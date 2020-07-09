package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;

public class ConstantVariableExpression extends VariableExpression {
    private IdentifierToken variableName;
    private ConstantNode defaultValue;
    public ConstantVariableExpression(IdentifierToken variableName, ConstantNode defaultValue) {
        super(variableName);
        this.variableName = variableName;
        this.defaultValue = defaultValue;
    }
    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {variableName};
    }

    public IdentifierToken getVariableName() {
        return variableName;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public Expression simplify() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "{CONST VAR: "+variableName+" - "+defaultValue+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        Object[] obj = defaultValue.buildJSON(parentId, nextId, builder);
        return obj;
    }
}
