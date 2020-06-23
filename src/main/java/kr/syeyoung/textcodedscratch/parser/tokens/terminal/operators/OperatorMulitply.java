package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import org.json.JSONObject;

public class OperatorMulitply extends OperatorNode {
    public OperatorMulitply() {
        super("*");
    }

    @Override
    public int getPriority() {
        return 3;
    }
    @Override
    public OperatorType getOperatorType() {
        return OperatorType.TWO_TERM;
    }
    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public ConstantNode operate(ConstantNode... nodes) {
        return new NumberToken(nodes[0].getValue(Double.class) * nodes[1].getValue(Double.class));
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_multiply")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("NUM1", terms[0])
                .input("NUM2", terms[1]).build();
    }
}
