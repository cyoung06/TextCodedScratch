package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ICodeContextConsumer;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.LocalVariableExpression;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;

public class VariableAssignment implements Statements, StackRequringOperation, ICodeContextConsumer {
    private VariableExpression variableExpression;
    private Expression expression;

    public VariableAssignment(VariableExpression expr, Expression expression) {
        this.variableExpression = expr;
        this.expression = expression;
    }

    private ICodeContext context;
    @Override
    public void setICodeContext(ICodeContext context) {
        this.context = context;
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
            String id = StackHelper.replaceStack(builder, parentId, nextId, currentStack - ((LocalVariableExpression) variableExpression).getDeclaration().getCurrentStack(), expression, context);
            return new String[] {id, id};
        } else {
            String id = builder.getNextID();
            Object[] expr = expression.buildJSON(id, null, builder);
            builder.putComplexObject(id, new ScratchBlockBuilder().op("data_setvariableto").nextId(nextId).parentId(parentId).input("VALUE", expr[0]).field("VARIABLE", new JSONArray().put(variableExpression.getVariableName().getMatchedStr()).put("$TCS_V$"+(variableExpression.isGlobal() ? "Stage" : variableExpression.getSpriteDefinition().getSpriteName().getName().getValue())+"$"+variableExpression.getVariableName().getMatchedStr())).shadow(false).topLevel(false).build());
            return new String[] {id, id};
        }
    }

    private int currentStack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.currentStack = stackSize;
        if (stackAtExe == -1)
            stackAtExe = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return currentStack;
    }

    private int stackAtExe = -1;
    @Override
    public int getStackCountAtExecution() {
        return stackAtExe;
    }
}
