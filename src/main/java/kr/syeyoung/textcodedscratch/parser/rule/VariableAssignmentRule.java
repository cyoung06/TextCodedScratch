package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.VariableAssignment;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorSet;

import java.util.Iterator;
import java.util.LinkedList;

public class VariableAssignmentRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {

        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof Expression && it.next() instanceof OperatorSet
                && it.next() instanceof VariableExpression) {
            ParserNode node1 = (ParserNode) it.next();
            if (!(node1 instanceof EOSToken)) throw new RuntimeException("Missing starting Semicolon at variable assignment");
            Expression token = (Expression) past.removeLast();
            past.removeLast();
            VariableExpression name = (VariableExpression) past.removeLast();
            past.removeLast();
            if (name.getVariableName() instanceof AccessedIdentifier) throw new RuntimeException("Variable name shouldn't be accessed identifier - "+name);
            future.addFirst(new VariableAssignment(name, token.simplify()));
            return true;
        }
        return false;
    }
}
