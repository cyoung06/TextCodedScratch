package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.OneTermedExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorNot;

public class WhileStatement implements Statements {
    private Expression condition;
    private Statements dowhile;
    public WhileStatement(Expression condition, Statements ifSo) {
        this.condition = new OneTermedExpression(condition, new OperatorNot()).simplify();
        this.dowhile = ifSo;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {condition, dowhile};
    }

    @Override
    public String toString() {
        return "{While cond: "+condition.toString()+" stmts: "+dowhile+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object[] conditionObj = condition.buildJSON(id, null, builder);
        Object[] stack1 = dowhile.buildJSON(id, null, builder);
        builder.putComplexObject(id, new ScratchBlockBuilder().op("control_repeat_until").nextId(nextId).parentId(parentId).input("CONDITION", conditionObj[0]).input("SUBSTACK", stack1[0]).shadow(false).topLevel(false).build());
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
