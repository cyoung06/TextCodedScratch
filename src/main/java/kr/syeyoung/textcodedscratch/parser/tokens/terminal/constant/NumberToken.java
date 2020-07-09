package kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import org.json.JSONArray;

public class NumberToken implements ConstantNode {
    private double value;
    private String matched;
    public NumberToken(String str) {
        this.matched = str;
        this.value = Double.parseDouble(str);
    }
    public NumberToken(double number) {
        this.matched = String.valueOf(number);
        this.value = number;
    }



    public NumberToken negate() {
        value = -value;
        return this;
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
        return "{Number: "+value+"}";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T getValue(Class<T> tClass) {
        if (tClass == Boolean.class) return (T) (value == 0 ? Boolean.FALSE : Boolean.TRUE);
        if (tClass == String.class) {
            String s = String.valueOf(value);
            return (T) (s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", ""));
        };
        if (tClass == Double.class) return (T) Double.valueOf(value);
        return null;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return new Object[] {new JSONArray().put(4).put(getValue()), new JSONArray().put(4).put(getValue())};
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
    public int getPriority() {
        return 4;
    }
}
