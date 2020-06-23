package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.EventDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeEventDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordEvent;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordNative;

import java.util.Iterator;
import java.util.LinkedList;

public class NativeEventDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof StringToken) {
            if (it.next() instanceof IdentifierToken && it.next() instanceof KeywordEvent && it.next() instanceof KeywordNative && it.next() instanceof EOSToken) {
                if (!(it.next() instanceof Declaration))
                    throw new RuntimeException("Native Event declaration should be after other declarations");
                StringToken json = (StringToken) past.removeLast();

                IdentifierToken identifierToken = (IdentifierToken) past.removeLast();
                past.removeLast();
                past.removeLast();
                past.removeLast();
                future.addFirst(new NativeEventDeclaration(identifierToken, json));
                return true;
            }
        }
        return false;
    }
}
