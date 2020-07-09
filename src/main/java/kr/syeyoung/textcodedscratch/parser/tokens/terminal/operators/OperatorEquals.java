package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.BooleanToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import org.json.JSONObject;

public class OperatorEquals extends OperatorNode {
    public OperatorEquals() {
        super("==");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_equals")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("OPERAND1", terms[0])
                .input("OPERAND2", terms[1]).build();
    }
    @Override
    public int getPriority() {
        return 1;
    }
    @Override
    public OperatorType getOperatorType() {
        return OperatorType.TWO_TERM;
    }
    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.BOOLEAN;
    }

    @Override
    public ConstantNode operate(ConstantNode... nodes) {
        ConstantNode par1 = nodes[0], par2 = nodes[1];
        return new BooleanToken(par1.getValue().equals(par2.getValue()));
    }
}
