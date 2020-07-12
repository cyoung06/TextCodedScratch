package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;

public class CommentToken implements TerminalNode, Statements {
    private String match;
    public CommentToken(String matched) {
        this.match = matched;
    }
    @Override
    public String getMatchedStr() {
        return match;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }


    @Override
    public int getStackCountAtExecution() {
        return stackAtExe;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return null;
    }

    private int stack;
    private int stackAtExe = -1;
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
}
