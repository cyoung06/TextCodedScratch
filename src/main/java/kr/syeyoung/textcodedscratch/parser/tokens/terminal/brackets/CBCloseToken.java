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
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return StackHelper.deallocateStack(builder, parentId, nextId, codeContext.getLocalStackSize());
    }
}
