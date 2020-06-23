package kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public class KeywordBoolean extends KeywordNode implements TypeKeywords {
    public KeywordBoolean() {
        super("Boolean");
    }

    @Override
    public FunctionParameter.ParameterType getType() {
        return FunctionParameter.ParameterType.BOOLEAN;
    }
}
