package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import org.json.JSONObject;

public class OperatorMinus extends OperatorNode {
    public OperatorMinus() {
        super("-");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_subtract")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("NUM1", terms.length == 1 ? new StringToken("\"\"") : terms[0])
                .input("NUM2", terms.length == 1 ? terms[0] : terms[1]).build();
    }
    @Override
    public int getPriority() {
        return 2;
    }
    @Override
    public OperatorType getOperatorType() {
        return OperatorType.DEPENDS;
    }
    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public ConstantNode operate(ConstantNode... nodes) {
        if (nodes.length == 1) return new NumberToken(-nodes[0].getValue(Double.class));
        return new NumberToken(nodes[0].getValue(Double.class) - nodes[1].getValue(Double.class));
    }
}
