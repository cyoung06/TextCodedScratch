package kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public class KeywordText extends KeywordNode implements TypeKeywords {
    public KeywordText() {
        super("Text");
    }

    @Override
    public FunctionParameter.ParameterType getType() {
        return FunctionParameter.ParameterType.TEXT;
    }
}
