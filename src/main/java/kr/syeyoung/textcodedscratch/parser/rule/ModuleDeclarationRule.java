package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.ModuleDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.SpriteDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.SOFToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordModule;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordSprite;

import java.util.Iterator;
import java.util.LinkedList;

public class ModuleDeclarationRule implements ParserRule {

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof KeywordModule) {
            if (!(it.next() instanceof SOFToken)) throw new RuntimeException("Module declaration should be right after start of the file without any lines");
            past.removeLast(); past.removeLast();
            future.addFirst(new ModuleDeclaration());
            return true;
        }
        return false;
    }
}
