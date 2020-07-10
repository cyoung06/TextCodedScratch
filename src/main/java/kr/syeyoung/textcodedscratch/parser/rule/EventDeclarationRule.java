package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.NativeFunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.EventDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.LocalVariableDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.NativeFunctionCallStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.VariableAssignment;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordEvent;

import java.util.*;

public class EventDeclarationRule implements ParserRule {
    private SyntexCheckerRule scr;
    public EventDeclarationRule(SyntexCheckerRule scr) {
        this.scr = scr;
    }

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof GroupedStatements) {
            boolean parameter = false;
            ParserNode pn = it.next();
            if (pn instanceof Expression) {
                parameter = true;
                pn = it.next();
            }
            if (pn instanceof IdentifierToken && it.next() instanceof KeywordEvent && it.next() instanceof EOSToken) {
                if (!(it.next() instanceof Declaration))
                    throw new RuntimeException("Event declaration should be after other declarations");
                GroupedStatements inside = (GroupedStatements) past.removeLast();
                Expression optionalToken = null;
                if (parameter) optionalToken = (Expression) past.removeLast();

                IdentifierToken identifierToken = (IdentifierToken) past.removeLast();
                past.removeLast();
                past.removeLast();

                if (!identifierToken.getMatchedStr().equals("Threads::onThreadCreation")) {
                    for (ParserNode stmt: inside.getChildren()) {
                        if (stmt instanceof LocalVariableDeclaration) throw new ParsingGrammarException("Can not use local variables in events. Please create a new thread using clone");
                        if (stmt instanceof FunctionCall && !(stmt instanceof NativeFunctionCall)) throw new ParsingGrammarException("Can not call functions within events");
                    }
                } else {
                    List<Statements> stmts = new ArrayList<>(Arrays.asList((Statements[])inside.getChildren()));
                    VariableExpression ex;
                    stmts.add(0, new VariableAssignment(ex = new VariableExpression(new IdentifierToken("$THREAD_IDX$"), false, false),new VariableExpression(new IdentifierToken("$THREAD_CNT$"), false, true)));
                    ex.setICodeContext(scr.getLastContext());
                    NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("deleteAll"), new FunctionParameter[0], new StringToken("\"{\"opcode\":\"data_deletealloflist\",\"next\":null,\"parent\":null,\"inputs\":{},\"fields\":{\"LIST\":[\"$THREAD_STACK$\",\"$TCS_L$$TCS_SPNAME$$$THREAD_STACK$\"]},\"shadow\":false,\"topLevel\":false}\""), false);
                    NativeFunctionCallStatement nfc = new NativeFunctionCallStatement(new IdentifierToken("deleteAll"), new Expression[0],nfd);
                    nfc.setICodeContext(scr.getLastContext());
                    stmts.add(0, nfc);

                    inside = new GroupedStatements(stmts.toArray(new Statements[0]));
                }

                future.addFirst(new EventDeclaration(identifierToken, inside, optionalToken));
                return true;
            }
        }
        return false;
    }
}
