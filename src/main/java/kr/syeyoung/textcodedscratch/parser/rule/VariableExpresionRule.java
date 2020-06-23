package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.AccesorToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TypeToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.POpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordNode;

import java.util.Iterator;
import java.util.LinkedList;

public class VariableExpresionRule implements ParserRule {

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (it.next() instanceof IdentifierToken && (!(it.next() instanceof KeywordNode)) && !(future.getFirst() instanceof POpenToken || future.getFirst() instanceof AccesorToken || future.getFirst() instanceof TypeToken)) {
            IdentifierToken identifierToken = (IdentifierToken) past.removeLast();
            future.addFirst(new VariableExpression(identifierToken));
            return true;
        }
        return false;
    }
}
