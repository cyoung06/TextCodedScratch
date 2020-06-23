package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNode;
import org.json.JSONObject;

public class TwoTermedExpression implements Expression {
    private Expression firstTerm;
    private OperatorNode operator;
    private Expression secondTerm;

    public Expression getFirstTerm() {
        return firstTerm;
    }

    public OperatorNode getOperator() {
        return operator;
    }

    public Expression getSecondTerm() {
        return secondTerm;
    }

    public TwoTermedExpression(Expression firstTerm, OperatorNode operator, Expression secondTerm) {
        this.firstTerm = firstTerm;
        this.operator = operator;
        this.secondTerm = secondTerm;
    }


    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return operator.getReturnType();
    }

    @Override
    public Expression simplify() {
        firstTerm = firstTerm.simplify();
        secondTerm = secondTerm.simplify();
        if (!(firstTerm instanceof ConstantNode)) return this;
        if (!(secondTerm instanceof ConstantNode)) return this;
        return operator.operate((ConstantNode) firstTerm, (ConstantNode) secondTerm);
    }

    @Override
    public String toString() {
        return "("+firstTerm+" : "+operator.getClass().getSimpleName() + " : "+secondTerm+")";
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {firstTerm, operator, secondTerm};
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object JfirstTerm = firstTerm.buildJSON(id,null,builder);
        Object JsecondTerm = secondTerm.buildJSON(id,null,builder);
        JSONObject obj = operator.operate(parentId, nextId, JfirstTerm, JsecondTerm);
        builder.putComplexObject(id, obj);
        return id;
    }

    @Override
    public int getPriority() {
        return operator.getPriority();
    }
}
