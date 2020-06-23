package kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TerminalNode;

public abstract class KeywordNode implements TerminalNode {
    private String keyword;
    public KeywordNode(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getMatchedStr() {
        return keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }
}
