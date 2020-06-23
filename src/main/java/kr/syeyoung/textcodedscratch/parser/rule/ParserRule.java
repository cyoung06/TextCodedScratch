package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;

import java.util.LinkedList;

public interface ParserRule {
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future);
}
