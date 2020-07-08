package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.GroupedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.IfStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordElse;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordIf;

import java.util.Iterator;
import java.util.LinkedList;

public class IfStatementRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        Iterator<ParserNode> fuit = future.iterator();
        if (it.next() instanceof Statements && it.next() instanceof GroupedExpression && it.next() instanceof KeywordIf && it.next() instanceof EOSToken) {
            ParserNode futureToken = fuit.hasNext() ? fuit.next() : null;
            if (!(futureToken instanceof EOSToken)) return false;
            futureToken = fuit.hasNext() ? fuit.next() : null;
            if (futureToken instanceof KeywordElse) return false;

            Statements stmts = (Statements) past.removeLast();
            Expression expr = (Expression) past.removeLast(); past.removeLast();past.removeLast();
            IfStatement is;
            future.addFirst(is = new IfStatement(expr.simplify(), stmts));
            if (expr instanceof StatementFormedListener)
                ((StatementFormedListener) expr).process(is, is, past, future);
            return true;
        } else if ((it = past.descendingIterator()) != null && it.next() instanceof GroupedExpression && it.next() instanceof KeywordIf && it.next() instanceof EOSToken) {
            past.add(new EOSToken("if"));
            return true;
        }
        return false;
    }
}
