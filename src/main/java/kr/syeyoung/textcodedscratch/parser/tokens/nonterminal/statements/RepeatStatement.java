package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;

public class RepeatStatement implements Statements {
    private Expression count;
    private Statements stmts;
    public RepeatStatement(Expression count, Statements stmts) {
        this.count = count;
        this.stmts = stmts;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {count, stmts};
    }

    @Override
    public String toString() {
        return "{Repeat count: "+count+" stmts: "+stmts+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object[] countObj = count.buildJSON(id, null, builder);
        Object[] stmtObj = stmts.buildJSON(id, null, builder);
        builder.putComplexObject(id, new ScratchBlockBuilder().op("control_repeat").nextId(nextId).parentId(parentId).input("TIMES", countObj[0]).input("SUBSTACK", stmtObj[0]).shadow(false).topLevel(false).build());
        return new String[] {id, id};
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
