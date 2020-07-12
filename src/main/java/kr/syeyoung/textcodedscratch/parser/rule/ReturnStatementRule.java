package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.GroupedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.RepeatStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.ReturnStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordRepeat;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordReturn;

import java.util.Iterator;
import java.util.LinkedList;

public class ReturnStatementRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (it.next() instanceof Expression && it.next() instanceof KeywordReturn && it.next() instanceof EOSToken) {
            ParserNode futureToken = future.size() != 0 ? future.getFirst() : null;
            if (!(futureToken instanceof EOSToken)) return false;

            Expression expr = (Expression) past.removeLast(); past.removeLast();past.removeLast();
            expr = expr.simplify();
            ReturnStatement rs;
            future.addFirst(rs = new ReturnStatement(expr));
            if (expr instanceof StatementFormedListener)
                ((StatementFormedListener) expr).process(rs, rs, past, future);
            return true;
        }
        it = past.descendingIterator();
        if (it.next() instanceof KeywordReturn && it.next() instanceof EOSToken) {
            ParserNode futureToken = future.size() != 0 ? future.getFirst() : null;
            if (!(futureToken instanceof EOSToken)) return false;

            past.removeLast();past.removeLast();
            ReturnStatement rs;
            future.addFirst(rs = new ReturnStatement(new StringToken("\"\"")));
            return true;
        }
        return false;
    }
}
