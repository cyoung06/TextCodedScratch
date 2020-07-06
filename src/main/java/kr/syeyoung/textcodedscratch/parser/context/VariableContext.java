package kr.syeyoung.textcodedscratch.parser.context;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;

import java.util.HashMap;
import java.util.Objects;

public class VariableContext implements ICodeContext {
    private HashMap<String, VariableDeclaration> variables = new HashMap<>();

    private ICodeContext parentContext;
    private int stack = 0;


    public VariableContext(ICodeContext parentContext) {
        this.parentContext = Objects.requireNonNull(parentContext);
    }

    @Override
    public boolean isVarialbeDefined(String variable) {
        return variables.containsKey(variable) || parentContext.isVarialbeDefined(variable);
    }

    @Override
    public VariableDeclaration getVariable(String variable) {
        if (variables.containsKey(variable)) return variables.get(variable);
        return parentContext.getVariable(variable);
    }

    @Override
    public void putVariable(VariableDeclaration variableDeclaration) {
        variables.put(Objects.requireNonNull(variableDeclaration).getName().getMatchedStr(), variableDeclaration);
    }

    @Override
    public int incrementStackCount() {
        ++stack;
        return getTotalStackSize();
    }

    @Override
    public int getLocalStackSize() {
        return stack;
    }

    @Override
    public int getTotalStackSize() {
        return stack + parentContext.getTotalStackSize();
    }
}
