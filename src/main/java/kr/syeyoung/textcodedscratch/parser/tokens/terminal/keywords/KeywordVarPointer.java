package kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public class KeywordVarPointer extends KeywordNode implements TypeKeywords {
    public KeywordVarPointer() {
        super("VariablePointer");
    }

    @Override
    public FunctionParameter.ParameterType getType() {
        return FunctionParameter.ParameterType.VARIABLE_POINTER;
    }
}
