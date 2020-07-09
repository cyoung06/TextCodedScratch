package kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets;

import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.context.VariableContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.MarkerToken;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;

public class CBCloseToken extends MarkerToken implements Statements {
    public CBCloseToken(String match) {
        super(match);
    }

    private ICodeContext codeContext;

    public void setICodeContext(ICodeContext context) {
        this.codeContext = context;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        System.out.println("Closing - "+codeContext.getLocalStackSize());
        if (codeContext.getLocalStackSize() == 0) return null;
        String id = StackHelper.deallocateStack(builder, parentId, nextId, codeContext.getLocalStackSize());
        return new Object[] {id, id};
    }


    private int stack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.stack = stackSize;
        if (stackAtExe == -1)
            stackAtExe = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return stack;
    }

    private int stackAtExe = -1;
    @Override
    public int getStackCountAtExecution() {
        return stackAtExe;
    }
}
