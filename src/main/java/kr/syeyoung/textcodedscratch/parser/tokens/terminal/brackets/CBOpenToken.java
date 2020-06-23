package kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets;

import kr.syeyoung.textcodedscratch.parser.context.IVariableContext;
import kr.syeyoung.textcodedscratch.parser.context.VariableContext;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.MarkerToken;

public class CBOpenToken extends MarkerToken {
    public CBOpenToken(String match) {
        super(match);
    }


    public IVariableContext createContext(IVariableContext parent) {
        return new VariableContext(parent);
    }
}
