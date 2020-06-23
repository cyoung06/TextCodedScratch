package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

public class MarkerToken implements TerminalNode {
    private String match;
    public MarkerToken(String match) {
        this.match = match;
    }

    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    @Override
    public String getMatchedStr() {
        return match;
    }
}
