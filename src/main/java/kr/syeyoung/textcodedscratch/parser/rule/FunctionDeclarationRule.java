package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.context.VariableContext;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.CommaToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.CBOpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.PCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.POpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordFunc;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordNative;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordReporter;

import java.util.Iterator;
import java.util.LinkedList;

public class FunctionDeclarationRule implements ParserRule {
    private SyntexCheckerRule scr;
    public FunctionDeclarationRule(SyntexCheckerRule scr) {
        this.scr = scr;
    }

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();

        if (past.getLast() instanceof KeywordFunc && !(future.getFirst() instanceof EOSToken)) {
            scr.getVariableContextQueue().addLast(new VariableContext(scr.getLastContext()));
            scr.setLastContext(scr.getVariableContextQueue().getLast());
        }


        if (!(future.getFirst() instanceof EOSToken)) return false;
        boolean possibleNative = false;
        {
            ParserNode pNode = it.next();
            if (!(pNode instanceof GroupedStatements || pNode instanceof StringToken)) return false;
            if (pNode instanceof StringToken) possibleNative = true;
        }
        if (!(it.next() instanceof PCloseToken)) return false;
        boolean weirdnessFound = false;
        int read = 4;
        int parametersCount = 0;
        boolean expectingComma = false;
        while (true) {
            if (!it.hasNext()) return false;
            ParserNode node = it.next();
            read++;
            if (node instanceof POpenToken) break;
            if (expectingComma && !(node instanceof CommaToken)) weirdnessFound = true;
            if (!expectingComma && !(node instanceof FunctionParameter)) weirdnessFound = true;
            if (node instanceof FunctionParameter) parametersCount++;
            expectingComma = !expectingComma;
        }
        ParserNode id;
        if (!((id = it.next()) instanceof IdentifierToken && it.next() instanceof KeywordFunc)) return false;
        boolean isNative = false;
        boolean isReporter = false;
        ParserNode ISnative = it.next();
        if (ISnative instanceof KeywordReporter) {
            isReporter = true;
            ISnative = it.next();
        }
        if (ISnative instanceof KeywordNative) {
            isNative = true;
            ISnative = it.next();
        }

        if (!(ISnative instanceof EOSToken)) return false;
        if (!(it.next() instanceof Declaration)) throw new RuntimeException("Function declaration should be after other declarations");
        if (weirdnessFound) throw new ParsingGrammarException("Malformed Function Declaration :: "+id+" :: Check parameters");
        if (isNative != possibleNative) throw new RuntimeException("Function declaration expected to be "+(possibleNative ? "Native" : "User Defined") + " But it was actually "+(isNative ? "Native" : "User Defined") );
        if (isNative) {
            StringToken json = (StringToken) past.removeLast();
            IdentifierToken identifierToken = null;
            FunctionParameter[] parameters = new FunctionParameter[parametersCount];
            for (int i = 0, j = parametersCount; i < read; i++) {
                ParserNode pn = past.removeLast();
                if (pn instanceof FunctionParameter) parameters[--j] = (FunctionParameter) pn;
                if (pn instanceof IdentifierToken) identifierToken = (IdentifierToken) pn;
            }
            future.addFirst(new NativeFunctionDeclaration(identifierToken, parameters, json, isReporter));



            scr.getVariableContextQueue().removeLast();
            scr.setLastContext(scr.getVariableContextQueue().getLast());
        } else {
            GroupedStatements inside = (GroupedStatements) past.removeLast();
            IdentifierToken identifierToken = null;
            FunctionParameter[] parameters = new FunctionParameter[parametersCount];
            for (int i = 0, j = parametersCount; i < read; i++) {
                ParserNode pn = past.removeLast();
                if (pn instanceof FunctionParameter) {
                    parameters[--j] = (FunctionParameter) pn;
                    if (((FunctionParameter) pn).getType() == FunctionParameter.ParameterType.VARIABLE_POINTER) {
                        throw new ParsingGrammarException("Can not use VARIABLE_POINTER type function in non-native Function declaration bc/ it is scratch");
                    }
                }
                if (pn instanceof IdentifierToken) identifierToken = (IdentifierToken) pn;
            }
            if (identifierToken instanceof AccessedIdentifier) throw new RuntimeException("Function name shouldn't be accessed identifier - "+identifierToken);
            future.removeFirst();
            future.addFirst(new CBOpenToken("{") {
                @Override
                public ICodeContext createContext(ICodeContext parent) {
                    ICodeContext context = super.createContext(parent);
                    for (FunctionParameter p:parameters)
                        context.putVariable(p);
                    return context;
                }
            });
            future.addFirst(new FunctionDeclaration(identifierToken, parameters, inside));

            scr.getVariableContextQueue().removeLast();
            scr.setLastContext(scr.getVariableContextQueue().getLast());
        }
        return true;
    }
}
