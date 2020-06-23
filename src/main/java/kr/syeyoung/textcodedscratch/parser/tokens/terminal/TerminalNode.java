package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

public interface TerminalNode extends ParserNode {
    public String getMatchedStr();
}
