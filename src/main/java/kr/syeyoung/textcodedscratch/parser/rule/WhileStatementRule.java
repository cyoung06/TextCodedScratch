package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.GroupedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.WhileStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordWhile;

import java.util.Iterator;
import java.util.LinkedList;

public class WhileStatementRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        Iterator<ParserNode> fuit = future.iterator();
        if (it.next() instanceof Statements && it.next() instanceof GroupedExpression && it.next() instanceof KeywordWhile && it.next() instanceof EOSToken) {
            ParserNode futureToken = fuit.hasNext() ? fuit.next() : null;
            if (!(futureToken instanceof EOSToken)) return false;

            Statements stmts = (Statements) past.removeLast();
            Expression expr = (Expression) past.removeLast(); past.removeLast();past.removeLast();
            WhileStatement ws;
            future.addFirst(ws = new WhileStatement(expr, stmts));
            if (expr instanceof StatementFormedListener)
                ((StatementFormedListener) expr).process(ws, ws, past, future);
            return true;
        } else if ((it = past.descendingIterator()) != null && it.next() instanceof GroupedExpression && it.next() instanceof KeywordWhile && it.next() instanceof EOSToken) {
            past.add(new EOSToken("while"));
            return true;
        }
        return false;
    }
}
