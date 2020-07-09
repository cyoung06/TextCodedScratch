package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import org.json.JSONArray;
import org.json.JSONObject;

public class OperatorGetName extends OperatorNode {
    public OperatorGetName() {
        super("&");
    }

    @Override
    public int getPriority() {
        return 4;
    }
    @Override
    public OperatorType getOperatorType() {
        return OperatorType.ONE_TERM;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.VARIABLE_POINTER;
    }

    @Override
    public ConstantNode operate(ConstantNode... nodes) {
        return null;
    }

    @Override
    public JSONObject operate(String parent, String next, Object... terms) {
        return null;
    }

    public JSONArray operate(VariableExpression varExpr) {
        String varName = varExpr.getVariableName().getMatchedStr();
        return new JSONArray().put(varName).put("$TCS_"+(varExpr.isList() ? "L": "V")+"$"+(varExpr.isGlobal() ? "Stage": varExpr.getSpriteDefinition().getSpriteName().getName().getValue())+"$"+varName);
    }
}
