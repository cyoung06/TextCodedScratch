package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.LocalVariableDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;

public class LocalVariableExpression extends VariableExpression  implements StackRequringOperation {
    public LocalVariableDeclaration getDeclaration() {
        return declaration;
    }

    private LocalVariableDeclaration declaration;
    public LocalVariableExpression(IdentifierToken variableName, LocalVariableDeclaration declaration) {
        super(variableName);
        this.declaration = declaration;
    }
    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String toString() {
        return "{LOCAL VAR: "+getVariableName()+"}";
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return StackHelper.accessStack(builder, parentId, nextId,  currentStack- declaration.getCurrentStack());
    }

    private int currentStack;

    @Override
    public void setCurrentStack(int stackSize) {
        currentStack = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return currentStack;
    }
}
