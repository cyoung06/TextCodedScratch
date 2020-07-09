package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;

public class LocalVariableDeclaration extends VariableDeclaration implements StackRequringOperation, Statements {
    private int currentStack;
    public LocalVariableDeclaration(IdentifierToken name, ConstantNode defaultValue) {
        super(false, name, defaultValue);
    }

    @Override
    public String toString() {
        return "Local Variable Declaration w/ name="+getName().getMatchedStr() + "&value="+getDefaultValue().getValue();
    }

    public int getCurrentStack() {
        return currentStack;
    }

    @Override
    public void setCurrentStack(int stackSize) {
        currentStack = stackSize;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = StackHelper.putStack(builder, parentId, nextId, getDefaultValue());
        return new Object[] {id, id};
    }
}
