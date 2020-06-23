package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

public class AccesorToken implements TerminalNode {
    private String match;
    public AccesorToken() {
        this.match = "::";
    }
    @Override
    public String getMatchedStr() {
        return match;
    }


    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }
}
