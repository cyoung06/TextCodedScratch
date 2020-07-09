package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.RequireDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordRequire;

import java.util.Iterator;
import java.util.LinkedList;

public class RequireDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof StringToken && it.next() instanceof KeywordRequire && it.next() instanceof EOSToken) {
            if (!(it.next() instanceof Declaration)) throw new RuntimeException("Require declaration should be after other declarations");
            StringToken first = (StringToken) past.removeLast();
            past.removeLast(); past.removeLast();
            future.addFirst(new RequireDeclaration(first));
            return true;
        }
        it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof IdentifierToken && it.next() instanceof KeywordRequire && it.next() instanceof EOSToken) {
            if (!(it.next() instanceof Declaration)) throw new RuntimeException("Require declaration should be after other declarations");
            StringToken first = new StringToken("\""+((IdentifierToken)past.removeLast()).getMatchedStr()+"\"");
            past.removeLast(); past.removeLast();
            future.addFirst(new RequireDeclaration(first));
            return true;
        }
        return false;
    }
}