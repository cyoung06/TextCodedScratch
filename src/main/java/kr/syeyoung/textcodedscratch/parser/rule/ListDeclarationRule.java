package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.ListDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.OneTermedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.CommaToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.SBCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.SBOpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordGlobal;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordList;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordVar;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorMinus;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorSet;

import java.util.Iterator;
import java.util.LinkedList;

public class ListDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {

        Iterator<ParserNode> it = past.descendingIterator();
        if (!(future.getFirst() instanceof EOSToken)) return false;
        if (!(future.getFirst() instanceof SBCloseToken)) return false;
        int read = 2;
        int expr = 0;
        boolean weirdnessFound = false;
        boolean expectingExpr = true;
        while (true) {
            if (!it.hasNext()) return false;
            ParserNode pn = it.next();
            read++;
            if (pn instanceof SBOpenToken) {
                if (!expectingExpr) weirdnessFound = true;
                break;
            }
            if (pn instanceof Expression) expr++;
            if (expectingExpr && !(pn instanceof Expression)) weirdnessFound = true;
            if (!expectingExpr && !(pn instanceof CommaToken)) weirdnessFound = true;
            expectingExpr = !expectingExpr;
        }
        if (!(it.next() instanceof OperatorSet && it.next() instanceof IdentifierToken && it.next() instanceof KeywordList)) return false;
        ParserNode node1 =it.next();
        boolean isGlobal = node1 instanceof KeywordGlobal;
        if (isGlobal) {
            read ++;
            node1 =  it.next();
        }
        if (!(node1 instanceof EOSToken && it.next() instanceof Declaration)) throw new ParsingGrammarException("List declaration should be after other declarations");
        if (weirdnessFound)  throw new ParsingGrammarException("Malformed list elements on list declaration");

        ConstantNode[] constantNodes = new ConstantNode[expr];
        for (int i =0, j=0; i< read; i++) {
            ParserNode pn = past.removeLast();
            if (pn instanceof Expression) {
                Expression simplify = ((Expression) pn).simplify();
                if (!(simplify instanceof ConstantNode)) throw new ParsingGrammarException("Can not include variabled expression inside list elements of list declaration");
                constantNodes[j++] = (ConstantNode) simplify;
            }
        }
        IdentifierToken id = (IdentifierToken) past.removeLast(); past.removeLast(); if (isGlobal) past.removeLast(); past.removeLast();


        if (id instanceof AccessedIdentifier) throw new ParsingGrammarException("Variable name shouldn't be accessed identifier - "+id);
        future.addFirst(new ListDeclaration(isGlobal, id, constantNodes));
        return true;
    }
}
