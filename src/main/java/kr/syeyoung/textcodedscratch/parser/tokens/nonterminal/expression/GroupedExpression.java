package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public class GroupedExpression implements Expression {
    private Expression expr;

    public GroupedExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public ParserNode[] getChildren() {
        return expr.getChildren();
    }

    @Override
    public String toString() {
        return "("+expr.toString()+")";
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return expr.getReturnType();
    }

    @Override
    public Expression simplify() {
        return expr.simplify();
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return expr.buildJSON(parentId, nextId, builder);
    }
}
