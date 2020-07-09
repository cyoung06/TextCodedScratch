package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.PCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.POpenToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorType;

import java.util.Iterator;
import java.util.LinkedList;

public class ExpressionReducingRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        // POSSIBLE INPUTS:: Expr Op / NonEXPR Op Expr / "(" Expr ")"
        Iterator<ParserNode> node = past.descendingIterator();
        ParserNode cTerm = node.next();
        ParserNode bTerm = node.hasNext() ? node.next() : null;
        ParserNode aTerm = node.hasNext() ? node.next() : null;

        if (cTerm instanceof Expression) {

            boolean isBTerm = bTerm instanceof OperatorNode;
            ParserNode lookAHead = future.size() == 0 ? null : future.get(0);

            boolean isFutureTerm = lookAHead instanceof OperatorNode;
            int priorityChosen = ((Expression) cTerm).getPriority();
            if (isBTerm != isFutureTerm) {
                if (isBTerm) priorityChosen= ((OperatorNode)bTerm).getPriority();
                else priorityChosen= ((OperatorNode)lookAHead).getPriority();
            } else if (!(isBTerm || isFutureTerm)) {
                return false;
            } else {
                int BTermPriority = ((OperatorNode)bTerm).getPriority();
                int FuturePriority = ((OperatorNode)lookAHead).getPriority();
                if (priorityChosen - BTermPriority > priorityChosen - FuturePriority) {
                    priorityChosen = FuturePriority;
                } else {
                    priorityChosen = BTermPriority;
                }
            }

            past.removeLast();
            past.add(new WrappingExpression((Expression) cTerm, priorityChosen));
            cTerm = past.getLast();
        }

        if (cTerm instanceof PCloseToken) {
            if (!(aTerm instanceof POpenToken)) return false;
            if (!(bTerm instanceof Expression)) return false;
            past.removeLast(); past.removeLast(); past.removeLast();
            future.addFirst(new GroupedExpression(bTerm instanceof WrappingExpression ? ((WrappingExpression) bTerm).getParent() : (Expression) bTerm));
            return true;
        } else if (cTerm instanceof Expression) {
            if (!(bTerm instanceof OperatorNode)) return false;
            OperatorNode operatorNode = (OperatorNode) bTerm;
            if (aTerm instanceof Expression) {
                if (operatorNode.getOperatorType() == OperatorType.ONE_TERM)  throw new ParsingGrammarException("Malformed Expression:: Bad use of Operator :: "+aTerm+ " - "+bTerm + " - "+cTerm);
                if (operatorNode.getPriority() != ((Expression) aTerm).getPriority() || operatorNode.getPriority() != ((Expression) cTerm).getPriority()) {
                    return false;
                }
                past.removeLast();past.removeLast();past.removeLast();
                future.addFirst(new TwoTermedExpression(aTerm instanceof WrappingExpression ? ((WrappingExpression) aTerm).getParent() : (Expression)aTerm, operatorNode,
                        cTerm instanceof WrappingExpression ? ((WrappingExpression) cTerm).getParent() : (Expression)cTerm));
            } else {
                if (operatorNode.getOperatorType() == OperatorType.TWO_TERM)  throw new ParsingGrammarException("Malformed Expression:: Bad use of Operator :: "+aTerm+ " - "+bTerm + " - "+cTerm);
                if (operatorNode.getPriority() != ((Expression) cTerm).getPriority()) {
                    return false;
                }
                past.removeLast();past.removeLast();
                future.addFirst(new OneTermedExpression(cTerm instanceof WrappingExpression ? ((WrappingExpression) cTerm).getParent() : (Expression)cTerm, operatorNode));
            }
        }
        return false;
    }
}
