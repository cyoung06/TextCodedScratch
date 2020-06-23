package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public class WrappingExpression implements Expression {
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
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return parent.buildJSON(parentId, nextId, builder);
    }

    @Override
    public String toString() {
        return "{"+parent.toString()+", "+priority+"}";
    }
}
