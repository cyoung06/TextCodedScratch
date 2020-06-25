package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import org.json.JSONObject;

public class OperatorConcat extends OperatorNode {
    public OperatorConcat() {
        super("..");
    }

    @Override
    public JSONObject operate(String parentId, String nextId, Object... terms) {
        return new ScratchBlockBuilder().op("operator_join")
                .nextId(nextId).parentId(parentId).shadow(false).topLevel(false)
                .input("STRING1", terms[0])
                .input("STRING2", terms[1]).build();
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
        return new StringToken("\""+nodes[0].getValue(String.class) + nodes[1].getValue(String.class)+"\"");
    }
}
