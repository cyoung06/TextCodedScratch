package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;

public class VariableExpression implements Expression {
    private IdentifierToken variableName;
    private boolean isList = false;
    public VariableExpression(IdentifierToken variableName) {
        this.variableName = variableName;
    }
    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {variableName};
    }

    public IdentifierToken getVariableName() {
        return variableName;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return "{VAR: "+variableName+"}";
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return new JSONArray().put(isList ? 13 : 12).put(variableName.getMatchedStr()).put("$TCS_"+(isList ? "L": "V")+"$_"+variableName.getMatchedStr());
    }
}
