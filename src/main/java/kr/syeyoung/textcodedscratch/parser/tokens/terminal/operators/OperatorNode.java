package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TerminalNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import org.json.JSONObject;

public abstract class  OperatorNode implements TerminalNode {
    private String operator;
    public OperatorNode(String operator) {
        this.operator = operator;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    @Override
    public String getMatchedStr() {
        return operator;
    }

    public abstract int getPriority();

    public abstract OperatorType getOperatorType();

    public abstract FunctionParameter.ParameterType getReturnType();

    public abstract ConstantNode operate(ConstantNode... nodes);

    public abstract JSONObject operate(String parent, String next, Object... terms);
}
