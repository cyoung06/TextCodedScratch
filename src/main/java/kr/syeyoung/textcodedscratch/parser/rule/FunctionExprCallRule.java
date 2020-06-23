package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.FunctionCallExpr;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.CommaToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.PCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.POpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class FunctionExprCallRule implements ParserRule {

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (!(it.next() instanceof PCloseToken)) return false;
        if (!(future.getFirst() instanceof PCloseToken || future.getFirst() instanceof OperatorNode || future.getFirst() instanceof EOSToken)) return false;
        int tokensRead = 2;
        boolean weirdnessFound = false;
        ArrayList<Expression> expressions = new ArrayList<>();
        boolean expectingComma = false;
        while (true) {
            if (!it.hasNext()) return false;
            ParserNode node = it.next();
            tokensRead ++;
            if (node instanceof POpenToken) break;
            if (expectingComma && !(node instanceof CommaToken)) weirdnessFound = true;
            if (!expectingComma && !(node instanceof Expression)) weirdnessFound = true;
            if (node instanceof Expression) expressions.add(((Expression) node).simplify());

            expectingComma = !expectingComma;
        }
        ParserNode id;
        if (!((id=it.next()) instanceof IdentifierToken)) return false;
        ParserNode next = it.hasNext() ? it.next() : null;
        if (next == null || !(next instanceof POpenToken || next instanceof OperatorNode || next instanceof OperatorSet || next instanceof CommaToken)) throw new ParsingGrammarException("Invalid location of function call :: "+id);

        if (weirdnessFound) throw new ParsingGrammarException("Invalid Function Call To :: "+id + " :: Check parameters");
        Collections.reverse(expressions);
        for (int i =0; i < tokensRead; i++) past.removeLast();
        future.addFirst(new FunctionCallExpr((IdentifierToken)id, expressions.toArray(new Expression[0])));
        return true;
    }
}
