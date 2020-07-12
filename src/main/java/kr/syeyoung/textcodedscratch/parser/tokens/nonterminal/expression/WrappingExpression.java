package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

import java.util.LinkedList;

public class WrappingExpression implements Expression, StatementFormedListener {
    private Expression parent;
    private int priority;
    public WrappingExpression(Expression expr, int priority) {
        this.priority = priority;
        this.parent = expr;
    }

    public Expression getParent() {
        if (parent instanceof WrappingExpression) return ((WrappingExpression) parent).getParent();
        else return parent;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return parent.getReturnType();
    }

    @Override
    public Expression simplify() {
        return parent.simplify();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public ParserNode[] getChildren() {
        return parent.getChildren();
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return parent.buildJSON(parentId, nextId, builder);
    }

    @Override
    public String toString() {
        return "{"+parent.toString()+", "+priority+"}";
    }

    @Override
    public void process(Statements formed, ParserNode parent, LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        if (parent instanceof StatementFormedListener)
            ((StatementFormedListener) parent).process(formed, parent, past, future);
    }

    @Override
    public void onStatementChange(Statements formed) {
        if (parent instanceof StatementFormedListener)
            ((StatementFormedListener) parent).onStatementChange(formed);
    }
}
