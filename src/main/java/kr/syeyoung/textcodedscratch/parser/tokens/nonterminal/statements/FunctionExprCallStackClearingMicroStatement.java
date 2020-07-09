package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackAddingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRemovingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FunctionExprCallStackClearingMicroStatement implements Statements, StackRemovingOperation, StackRequringOperation {
    private Statements stmt;


    public FunctionExprCallStackClearingMicroStatement (Statements stmt) {
        this.stmt = stmt;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    // {"tagName":"mutation","children":[],"proccode":"asdasd %s %s","argumentids":"[\"input1\",\"input2\"]","warp":"false"}}
    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id = StackHelper.deallocateStackOffset(builder, parentId, nextId, 1);
        return new Object[] {id, id};
    }


    private int stack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.stack = stackSize;
        if (stmt instanceof StackRequringOperation)
            ((StackRequringOperation) stmt).setCurrentStack(((StackRequringOperation) stmt).getCurrentStack() - 1);
    }

    @Override
    public int getCurrentStack() {
        return stack;
    }
}
