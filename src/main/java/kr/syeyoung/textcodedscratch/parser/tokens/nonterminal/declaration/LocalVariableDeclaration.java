package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ICodeContextConsumer;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackAddingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;

public class LocalVariableDeclaration extends VariableDeclaration implements StackRequringOperation, Statements, StackAddingOperation, ICodeContextConsumer {
    private int currentStack;
    private Expression defaultValue;
    public LocalVariableDeclaration(IdentifierToken name, Expression defaultValue) {
        super(false, name, new StringToken("\"\""));
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "Local Variable Declaration w/ name="+getName().getMatchedStr() + "&value="+getDefaultValue().getValue();
    }

    public Expression getExpr() {
        return defaultValue;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {getName(), defaultValue};
    }

    public int getCurrentStack() {
        return currentStack;
    }

    @Override
    public void setCurrentStack(int stackSize) {
        currentStack = stackSize;
        if (stackAtExe == -1)
            stackAtExe = stackSize;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = StackHelper.putStack(builder, parentId, nextId, defaultValue, context);
        return new Object[] {id, id};
    }



    private int stackAtExe = -1;
    @Override
    public int getStackCountAtExecution() {
        return stackAtExe;
    }

    private ICodeContext context;
    @Override
    public void setICodeContext(ICodeContext context) {
        this.context = context;
    }
}
