package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import org.json.JSONObject;

public class OperatorPlus extends OperatorNode {
    public OperatorPlus() {
        super("+");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_add")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("NUM1", terms[0])
                .input("NUM2", terms[1]).build();
    }
    @Override
    public int getPriority() {
        return 2;
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
        if (nodes[0] instanceof StringToken) return new StringToken("\""+nodes[0].getValue(String.class) + nodes[1].getValue()+"\"");
        return new NumberToken(nodes[0].getValue(Double.class) + nodes[1].getValue(Double.class));
    }
}
