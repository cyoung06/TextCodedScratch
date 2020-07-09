package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StatementFormedListener;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorGetName;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNode;
import org.json.JSONObject;

import java.util.LinkedList;

public class OneTermedExpression implements Expression, StatementFormedListener {
    private Expression firstTerm;
    private OperatorNode operator;

    public Expression getFirstTerm() {
        return firstTerm;
    }

    public OperatorNode getOperator() {
        return operator;
    }

    public OneTermedExpression(Expression firstTerm, OperatorNode operator) {
        this.firstTerm = firstTerm;
        this.operator = operator;
    }


    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return operator.getReturnType();
    }

    @Override
    public Expression simplify() {
        if (operator instanceof OperatorGetName) {
            if (!(firstTerm instanceof VariableExpression)) throw new ParsingGrammarException("After the Pointer Operator, there should Only be identifier name");
            if (firstTerm instanceof ConstantVariableExpression) throw new ParsingGrammarException("No pointers for constant variable");
            return firstTerm;
        }

        firstTerm = firstTerm.simplify();
        if (!(firstTerm instanceof ConstantNode)) return this;
        return operator.operate((ConstantNode) firstTerm);
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {operator, firstTerm};
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        if (operator instanceof OperatorGetName)
            return new Object[] {((OperatorGetName) operator).operate((VariableExpression) firstTerm),((OperatorGetName) operator).operate((VariableExpression) firstTerm)};

        String id = builder.getNextID();
        Object[] JfirstTerm = firstTerm.buildJSON(id,null,builder);
        JSONObject obj = operator.operate(parentId, nextId, JfirstTerm[0]);
        builder.putComplexObject(id, obj);
        return new Object[] {id, id};
    }

    @Override
    public String toString() {
        return "("+operator.getClass().getSimpleName() + ": "+firstTerm+")";
    }

    @Override
    public int getPriority() {
        return operator.getPriority();
    }


    @Override
    public void process(Statements formed, ParserNode parent, LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        if (firstTerm instanceof StatementFormedListener)
            ((StatementFormedListener) firstTerm).process(formed, this, past, future);
    }
}
