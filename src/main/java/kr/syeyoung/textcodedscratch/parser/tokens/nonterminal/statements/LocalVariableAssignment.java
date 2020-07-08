package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import org.json.JSONArray;

public class LocalVariableAssignment implements Statements {
    private VariableExpression variableExpression;
    private Expression expression;

    public LocalVariableAssignment(VariableExpression expr, Expression expression) {
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
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object expr = expression.buildJSON(id, null, builder);
        builder.putComplexObject(id, new ScratchBlockBuilder().op("data_setvariableto").nextId(nextId).parentId(parentId).input("VALUE", expr).field("VARIABLE", new JSONArray().put(variableExpression.getVariableName().getMatchedStr()).put("$TCS_V$_"+variableExpression.getVariableName().getMatchedStr())).shadow(false).topLevel(false).build());
        return id;
    }
}
