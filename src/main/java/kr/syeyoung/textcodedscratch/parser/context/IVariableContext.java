package kr.syeyoung.textcodedscratch.parser.context;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;

public interface IVariableContext {
    public boolean isDefined(String variable);

    public VariableDeclaration getVariable(String variable);

    public void putVariable(VariableDeclaration variableDeclaration);
}
