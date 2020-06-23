package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.BooleanToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import org.json.JSONObject;

public class OperatorNot extends OperatorNode {
    public OperatorNot() {
        super("!");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_not")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("OPERAND", terms[0]).build();
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
        return FunctionParameter.ParameterType.BOOLEAN;
    }

    @Override
    public ConstantNode operate(ConstantNode... nodes) {
        return new BooleanToken(!nodes[1].getValue(Boolean.class));
    }
}
