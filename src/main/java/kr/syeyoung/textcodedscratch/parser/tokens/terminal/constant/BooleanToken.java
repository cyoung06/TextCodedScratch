package kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import org.json.JSONArray;
import org.json.JSONObject;

public class BooleanToken implements ConstantNode {
    private boolean value;
    private String matched;
    public BooleanToken(String str) {
        super();
        this.matched = str;
        this.value = Boolean.parseBoolean(str);
    }

    public BooleanToken(boolean bool) {
        super();
        this.matched = String.valueOf(bool);
        this.value = bool;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T getValue(Class<T> tClass) {
        if (tClass == Boolean.class) return (T) Boolean.valueOf(value);
        if (tClass == String.class) return (T) String.valueOf(value);
        if (tClass == Double.class) return (T) (value ? Double.valueOf(1): Double.valueOf(0));
        return null;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.putComplexObject(new JSONObject().put("opcode", value ? "operator_not" : "operator_or").put("next", nextId == null ? JSONObject.NULL : nextId).put("parent", parentId == null ? JSONObject.NULL : parentId).put("inputs", new JSONObject()).put("fields", new JSONObject()).put("shadow", false).put("topLevel", false));
        return new Object[] {id, id};
    }


    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.BOOLEAN;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String getMatchedStr() {
        return matched;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    @Override
    public String toString() {
        return "{Boolean: "+value+"}";
    }
    @Override
    public int getPriority() {
        return 4;
    }
}
