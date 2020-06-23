package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.BooleanToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import org.json.JSONObject;

public class OperatorOr extends OperatorNode {
    public OperatorOr() {
        super("||");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_or")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("OPERAND1", terms[0])
                .input("OPERAND2", terms[1]).build();
    }
    @Override
    public int getPriority() {
        return 0;
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
        return new BooleanToken(nodes[0].getValue(Boolean.class) || nodes[1].getValue(Boolean.class));
    }
}
