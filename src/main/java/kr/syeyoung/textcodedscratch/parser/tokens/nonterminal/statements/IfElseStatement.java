package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;

public class IfElseStatement implements Statements {
    private Expression condition;
    private Statements ifSo;
    private Statements elseThen;
    public IfElseStatement(Expression condition, Statements ifSo, Statements elsethen) {
        this.condition = condition;
        this.ifSo = ifSo;
        this.elseThen = elsethen;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {condition, ifSo, elseThen};
    }

    @Override
    public String toString() {
        return "{IfElse cond: "+condition.toString()+" if: "+ifSo+", else: "+elseThen+"}";
    }


    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object[] conditionObj = condition.buildJSON(id, null, builder);
        Object[] stack1 = ifSo.buildJSON(id, null, builder);
        Object[] stack2 = elseThen.buildJSON(id, null, builder);
        builder.putComplexObject(id, new ScratchBlockBuilder().op("control_if_else").nextId(nextId).parentId(parentId).input("CONDITION", conditionObj[0]).input("SUBSTACK", stack1[0]).input("SUBSTACK2",stack2[0]).shadow(false).topLevel(false).build());
        return new Object[] {id, id};
    }
}
