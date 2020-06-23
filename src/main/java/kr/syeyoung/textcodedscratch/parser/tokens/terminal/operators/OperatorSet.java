package kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TerminalNode;
import org.json.JSONArray;

public class OperatorSet implements TerminalNode {
    public OperatorSet() {    }
    @Override
    public String getMatchedStr() {
        return "=";
    }


    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }
}
