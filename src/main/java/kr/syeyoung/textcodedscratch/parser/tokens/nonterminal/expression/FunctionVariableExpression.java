package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;
import org.json.JSONObject;

public class FunctionVariableExpression extends VariableExpression {
    private IdentifierToken variableName;
    private FunctionParameter parameterDef;
    private Expression proxyExpr;

    public Expression getProxyExpr() {
        return proxyExpr;
    }

    public void setProxyExpr(Expression proxyExpr) {
        this.proxyExpr = proxyExpr;
    }

    public FunctionVariableExpression(IdentifierToken variableName, FunctionParameter parameterDef) {
        super(variableName);
        this.variableName = variableName;
        this.parameterDef = parameterDef;
    }
    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public ParserNode[] getChildren() {
        return proxyExpr == null ? new ParserNode[] {variableName}: proxyExpr.getChildren();
    }

    public IdentifierToken getVariableName() {
        return variableName;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return proxyExpr == null ? parameterDef.getType() : proxyExpr.getReturnType();
    }

    @Override
    public Expression simplify() {
        return proxyExpr == null ? this : proxyExpr.simplify();
    }

    @Override
    public String toString() {
        return "{FUNCTION VAR: "+variableName+" proxied to "+proxyExpr+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        if (proxyExpr != null) return proxyExpr.buildJSON(parentId, nextId, builder);
        String ID = builder.putComplexObject(new ScratchBlockBuilder().op(parameterDef.getType() == FunctionParameter.ParameterType.TEXT ? "argument_reporter_string_number" : "argument_reporter_boolean").nextId(nextId).parentId(parentId).field("VALUE", new JSONArray().put(getVariableName().getMatchedStr()).put(JSONObject.NULL)).shadow(false).topLevel(false).build());
        return new Object[] {ID, ID};
    }
}
