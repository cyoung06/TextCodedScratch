package kr.syeyoung.textcodedscratch.parser.context;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;

import java.util.HashMap;
import java.util.Objects;

public class VariableContext implements IVariableContext {
    private HashMap<String, VariableDeclaration> variables = new HashMap<>();

    private IVariableContext parentContext;

    public VariableContext(IVariableContext parentContext) {
        this.parentContext = Objects.requireNonNull(parentContext);
    }

    @Override
    public boolean isDefined(String variable) {
        return variables.containsKey(variable) || parentContext.isDefined(variable);
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
}
