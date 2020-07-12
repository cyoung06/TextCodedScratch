package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNode;
import org.json.JSONObject;

import java.util.LinkedList;

public class TwoTermedExpression implements Expression, StatementFormedListener {
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
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object[] JfirstTerm = firstTerm.buildJSON(id,null,builder);
        Object[] JsecondTerm = secondTerm.buildJSON(id,null,builder);
        JSONObject obj = operator.operate(parentId, nextId, JfirstTerm[0], JsecondTerm[0]);
        builder.putComplexObject(id, obj);
        return new Object[] {id,id};
    }

    @Override
    public int getPriority() {
        return operator.getPriority();
    }


    @Override
    public void process(Statements formed, ParserNode parent, LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        if (firstTerm instanceof StatementFormedListener)
            ((StatementFormedListener) firstTerm).process(formed, this, past, future);
        if (secondTerm instanceof StatementFormedListener)
            ((StatementFormedListener) secondTerm).process(formed, this, past, future);
    }

    @Override
    public void onStatementChange(Statements formed) {
        if (firstTerm instanceof StatementFormedListener)
            ((StatementFormedListener) firstTerm).onStatementChange(formed);
        if (secondTerm instanceof StatementFormedListener)
            ((StatementFormedListener) secondTerm).onStatementChange(formed);
    }
}
