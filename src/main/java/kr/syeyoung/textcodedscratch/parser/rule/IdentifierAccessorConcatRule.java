package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.AccesorToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;

import java.util.Iterator;
import java.util.LinkedList;

public class IdentifierAccessorConcatRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (it.next() instanceof IdentifierToken && it.next() instanceof AccesorToken && it.next() instanceof IdentifierToken) {
            IdentifierToken child = (IdentifierToken) past.removeLast();past.removeLast();
            IdentifierToken parent = (IdentifierToken) past.removeLast();
            future.addFirst(new AccessedIdentifier(parent, child));
            return true;
        }
        return false;
    }
}
