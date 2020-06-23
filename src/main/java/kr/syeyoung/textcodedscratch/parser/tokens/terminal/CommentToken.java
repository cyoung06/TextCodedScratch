package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

public class CommentToken implements TerminalNode {
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
}
