package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.OneTermedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordGlobal;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordVar;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorMinus;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorSet;

import java.util.Iterator;
import java.util.LinkedList;

public class VariableDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {

        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof Expression && it.next() instanceof OperatorSet
                && it.next() instanceof IdentifierToken && it.next() instanceof KeywordVar) {
            ParserNode node1 = (ParserNode) it.next();;
            boolean isGlobal = node1 instanceof KeywordGlobal;
            if (isGlobal) node1 = (ParserNode) it.next();
            if (!(node1 instanceof EOSToken && it.next() instanceof Declaration)) throw new ParsingGrammarException("Variable declaration should be after other declarations");
            Expression token = (Expression) past.removeLast();
            Expression simplified = token.simplify();
            if (!(simplified instanceof ConstantNode)) throw new ParsingGrammarException("No variabled expression inside variable declaration");
            past.removeLast();
            IdentifierToken name = (IdentifierToken) past.removeLast();
            past.removeLast(); if (isGlobal) past.removeLast();
            past.removeLast();
            if (name instanceof AccessedIdentifier) throw new ParsingGrammarException("Variable name shouldn't be accessed identifier - "+name);
            future.addFirst(new VariableDeclaration(isGlobal, name, (ConstantNode) simplified));
            return true;
        }
        return false;
    }
}
