package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import org.json.JSONObject;

public class OperatorDivide extends OperatorNode {
    public OperatorDivide() {
        super("/");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_divide")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("NUM1", terms[0])
                .input("NUM2", terms[1]).build();
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
        return new NumberToken(nodes[0].getValue(Double.class) / nodes[1].getValue(Double.class));
    }
}
