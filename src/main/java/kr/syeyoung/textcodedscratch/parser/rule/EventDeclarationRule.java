package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.EventDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.Declaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class EventDeclarationRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (future.getFirst() instanceof EOSToken && it.next() instanceof GroupedStatements) {
            boolean parameter = false;
            ParserNode pn = it.next();
            if (pn instanceof Expression) {
                parameter = true;
                pn = it.next();
            }
            if (pn instanceof IdentifierToken && it.next() instanceof KeywordEvent && it.next() instanceof EOSToken) {
                if (!(it.next() instanceof Declaration))
                    throw new RuntimeException("Event declaration should be after other declarations");
                GroupedStatements inside = (GroupedStatements) past.removeLast();
                Expression optionalToken = null;
                if (parameter) optionalToken = (Expression) past.removeLast();

                IdentifierToken identifierToken = (IdentifierToken) past.removeLast();
                past.removeLast();
                past.removeLast();

                future.addFirst(new EventDeclaration(identifierToken, inside, optionalToken));
                return true;
            }
        }
        return false;
    }
}
