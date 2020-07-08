package kr.syeyoung.textcodedscratch.parser.context;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;

public interface ICodeContext {
    public boolean isVarialbeDefined(String variable);

    public VariableDeclaration getVariable(String variable);

    public void putVariable(VariableDeclaration variableDeclaration);

    public int incrementStackCount();
    public int decrementStackCount();
    public int getLocalStackSize();
    public int getTotalStackSize();
}
