package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ICodeContextConsumer;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReturnStatement implements Statements, ICodeContextConsumer {
    private Expression returnValue;
    public ReturnStatement(Expression returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {returnValue};
    }

    @Override
    public String toString() {
        return "{Return value: "+returnValue+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = StackHelper.putStack(builder, parentId, null, returnValue, context);


        ScratchBlockBuilder stop = new ScratchBlockBuilder().op("control_stop").nextId(nextId).parentId(null).field("STOP_OPTION", new JSONArray().put("this script").put(JSONObject.NULL)).shadow(false).topLevel(false).put("mutation", new JSONObject().put("tagName", "mutation").put("children", new JSONArray()).put("hasnext", false));

        String last = "";
        if (context.getTotalStackSize() != 0) {
            String nextId2 = StackHelper.deallocateStackOffset(builder, id, null, 1, stack, context);
            builder.getComplexObject(id).put("next", nextId2);

            String nextId3 = builder.putComplexObject(stop.parentId(nextId2).build());
            builder.getComplexObject(nextId2).put("next", nextId3);
            last = nextId3;
        } else {
            String nextId2 = builder.putComplexObject(stop.parentId(id).build());
            builder.getComplexObject(id).put("next", nextId2);
            last = nextId2;
        }


        return new String[] {id, last};
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


    private ICodeContext context;
    @Override
    public void setICodeContext(ICodeContext context) {
        this.context = context;
    }
}
