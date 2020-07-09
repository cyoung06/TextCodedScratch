package kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import org.json.JSONArray;

public class StringToken implements ConstantNode {
    private String str;

    public StringToken(String str) {
        this.str = str;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return new Object[] {new JSONArray().put(10).put(getValue()), new JSONArray().put(10).put(getValue())};
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    @Override
    public Object getValue() {
        return str.substring(1, str.length() - 1);
    }

    @Override
    public <T> T getValue(Class<T> tClass) {
        if (tClass == Boolean.class) return (T) (str.substring(1, str.length() - 1).isEmpty() ? Boolean.FALSE : Boolean.TRUE);
        if (tClass == String.class) return (T) str.substring(1, str.length() - 1);
        if (tClass == Double.class) return (T) Double.valueOf(0);
        return null;
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
    public String getMatchedStr() {
        return str;
    }

    @Override
    public String toString() {
        return "{String: "+getValue()+"}";
    }
    @Override
    public int getPriority() {
        return 4;
    }
}
