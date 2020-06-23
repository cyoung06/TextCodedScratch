package kr.syeyoung.textcodedscratch.parser.rule;


import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOFToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;

import java.util.Iterator;
import java.util.LinkedList;

public class EOSConcatRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (it.next() instanceof EOSToken && it.next() instanceof EOSToken) {
            it = past.descendingIterator();
            EOSToken first = (EOSToken) past.removeLast();
            EOSToken second = (EOSToken) past.removeLast();
            future.addFirst(new EOSToken(first.getMatchedStr() + second.getMatchedStr()));
            return true;
        }
        return false;
    }
}
