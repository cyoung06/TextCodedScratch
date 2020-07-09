package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.LocalVariableExpression;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;

public class VariableAssignment implements Statements, StackRequringOperation {
    private VariableExpression variableExpression;
    private Expression expression;

    public VariableAssignment(VariableExpression expr, Expression expression) {
        this.variableExpression = expr;
        this.expression = expression;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {variableExpression, expression};
    }

    @Override
    public String toString() {
        return "{Variable Assignment: "+variableExpression+" to "+expression+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        if (variableExpression instanceof LocalVariableExpression) {
            String id = StackHelper.replaceStack(builder, parentId, nextId, currentStack - ((LocalVariableExpression) variableExpression).getDeclaration().getCurrentStack(), expression);
            return new String[] {id, id};
        } else {
            String id = builder.getNextID();
            Object[] expr = expression.buildJSON(id, null, builder);
            builder.putComplexObject(id, new ScratchBlockBuilder().op("data_setvariableto").nextId(nextId).parentId(parentId).input("VALUE", expr[0]).field("VARIABLE", new JSONArray().put(variableExpression.getVariableName().getMatchedStr()).put("$TCS_V$_"+variableExpression.getVariableName().getMatchedStr())).shadow(false).topLevel(false).build());
            return new String[] {id, id};
        }
    }

    private int currentStack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.currentStack = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return currentStack;
    }
}
