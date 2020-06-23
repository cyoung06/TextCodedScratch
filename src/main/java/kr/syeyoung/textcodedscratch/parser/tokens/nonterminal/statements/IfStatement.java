package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;

public class IfStatement implements Statements {
    private Expression condition;
    private Statements ifSo;
    public IfStatement(Expression condition, Statements ifSo) {
        this.condition = condition;
        this.ifSo = ifSo;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {condition, ifSo};
    }

    @Override
    public String toString() {
        return "{If cond: "+condition.toString()+" if: "+ifSo+"}";
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = builder.getNextID();
        Object conditionObj = condition.buildJSON(id, null, builder);
        Object stack1 = ifSo.buildJSON(id, null, builder);
        builder.putComplexObject(id, new ScratchBlockBuilder().op("control_if").nextId(nextId).parentId(parentId).input("CONDITION", conditionObj).input("SUBSTACK", stack1).shadow(false).topLevel(false).build());
        return id;
    }
}
