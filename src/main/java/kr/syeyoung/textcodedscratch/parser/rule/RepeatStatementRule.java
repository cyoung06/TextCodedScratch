package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.GroupedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.RepeatStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordRepeat;

import java.util.Iterator;
import java.util.LinkedList;

public class RepeatStatementRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        Iterator<ParserNode> fuit = future.iterator();
        if (it.next() instanceof Statements && it.next() instanceof GroupedExpression && it.next() instanceof KeywordRepeat && it.next() instanceof EOSToken) {
            ParserNode futureToken = fuit.hasNext() ? fuit.next() : null;
            if (!(futureToken instanceof EOSToken)) return false;

            Statements stmts = (Statements) past.removeLast();
            Expression expr = (Expression) past.removeLast(); past.removeLast();past.removeLast();
            future.addFirst(new RepeatStatement(expr.simplify(), stmts));
            return true;
        } else if ((it = past.descendingIterator()) != null && it.next() instanceof GroupedExpression && it.next() instanceof KeywordRepeat && it.next() instanceof EOSToken) {
            past.add(new EOSToken("repeat"));
            return true;
        }
        return false;
    }
}
