package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.CostumeDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordCostume;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorSet;

import java.util.Iterator;
import java.util.LinkedList;

public class CostumeDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof StringToken && it.next() instanceof OperatorSet
                && it.next() instanceof IdentifierToken && it.next() instanceof KeywordCostume && it.next() instanceof EOSToken) {
            if (!(it.next() instanceof Declaration)) throw new ParsingGrammarException("Costume declaration should be after other declarations");
            StringToken token = (StringToken) past.removeLast();
            past.removeLast();
            IdentifierToken name = (IdentifierToken) past.removeLast();
            past.removeLast(); past.removeLast();
            if (name instanceof AccessedIdentifier) throw new RuntimeException("Costume name shouldn't be accessed identifier - "+name);
            future.addFirst(new CostumeDeclaration(name, token));
            return true;
        }
        return false;
    }
}
