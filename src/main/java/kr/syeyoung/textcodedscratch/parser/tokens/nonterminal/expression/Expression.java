package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;

public interface Expression extends ParserNode, ScratchTransferable {
    public int getPriority();

    public FunctionParameter.ParameterType getReturnType();

    public Expression simplify();
}
