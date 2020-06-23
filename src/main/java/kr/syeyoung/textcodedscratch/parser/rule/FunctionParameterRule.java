package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.AccessedIdentifier;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.VariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TypeToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordBoolean;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.TypeKeywords;

import java.util.Iterator;
import java.util.LinkedList;

public class FunctionParameterRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> it = past.descendingIterator();
        if (it.next() instanceof TypeKeywords && it.next() instanceof TypeToken && it.next() instanceof IdentifierToken) {
            FunctionParameter.ParameterType type = ((TypeKeywords)past.removeLast()).getType(); past.removeLast();
            IdentifierToken expr = (IdentifierToken) past.removeLast();
            if (expr instanceof AccessedIdentifier) throw new ParsingGrammarException("variable name shouldn't be accessed identifier - "+ expr.getMatchedStr());

            future.addFirst(new FunctionParameter(expr, type));
            return true;
        }
        return false;
    }
}
