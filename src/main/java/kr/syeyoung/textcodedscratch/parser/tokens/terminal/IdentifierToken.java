package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

public class IdentifierToken implements TerminalNode {
    private String match;
    public IdentifierToken(String matched) {
        this.match = matched;
    }
    @Override
    public String getMatchedStr() {
        return match;
    }

    @Override
    public String toString() {
        return "{Identifier: "+match+"}";
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }
}
