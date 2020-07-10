package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.SOFToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordSprite;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordStage;

import java.util.Iterator;
import java.util.LinkedList;

public class SpriteDeclarationRule implements ParserRule {

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof StringToken && it.next().getClass() == KeywordSprite.class ) {
            if (!(it.next() instanceof SOFToken)) throw new RuntimeException("Sprite declaration should be right after start of the file without any lines");
            StringToken first = (StringToken) past.removeLast();
            past.removeLast(); past.removeLast();;
            future.addFirst(new VariableDeclaration(false, new IdentifierToken("$THREAD_IDX$"), new NumberToken(0.0)));
            future.addFirst(new VariableDeclaration(true, new IdentifierToken("$THREAD_CNT$"), new NumberToken(0.0)));
            future.addFirst(new StackDeclaration(new IdentifierToken("$THREAD_STACK$")));
            future.addFirst(new SpriteDeclaration(first));
            return true;
        }
        it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof KeywordStage) {
            if (!(it.next() instanceof SOFToken)) throw new RuntimeException("Stage declaration should be right after start of the file without any lines");
            past.removeLast(); past.removeLast();
            future.addFirst(new StageDeclaration());
            return true;
        }
        return false;
    }
}
