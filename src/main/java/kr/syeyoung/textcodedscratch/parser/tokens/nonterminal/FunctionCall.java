package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;

public interface FunctionCall {

    public IdentifierToken getFunctionName();
    public Expression[] getParameters();

    public void setFunctionDeclaration(FunctionDeclaration functionDeclaration);

}
