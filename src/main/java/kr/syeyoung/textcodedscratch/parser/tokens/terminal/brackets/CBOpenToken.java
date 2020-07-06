package kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets;

import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.context.VariableContext;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.MarkerToken;

public class CBOpenToken extends MarkerToken {
    public CBOpenToken(String match) {
        super(match);
    }


    public ICodeContext createContext(ICodeContext parent) {
        return new VariableContext(parent);
    }
}
