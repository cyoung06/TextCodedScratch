package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.CBCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.CBOpenToken;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class StatementGroupingRule implements ParserRule {
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        Iterator<ParserNode> pNodes = past.descendingIterator();
        if (!(pNodes.next() instanceof CBCloseToken)) return false;
        int nodes = 0;
        int stmts = 1;
        while(pNodes.hasNext()) {
            try {
                ParserNode node = pNodes.next();
                if (node instanceof CBOpenToken) break;
                if (!(node instanceof Statements || node instanceof EOSToken)) throw new ParsingGrammarException("There should ONLY Be statements or empty lines inside statementgroup :: "+node);
                nodes++;
                if (node instanceof Statements) stmts++;
            } catch (NoSuchElementException exception) {
                throw new ParsingGrammarException("No Matching Bracket found for }");
            }
        }
        if (pNodes.hasNext() && pNodes.next() instanceof EOSToken) nodes++;

        Statements[] nodeList = new Statements[stmts];
        for (int i = 0, j = stmts; i < nodes + 2; i++) {
            ParserNode node = past.removeLast();
            if (node instanceof Statements) nodeList[--j] = (Statements) node;
        }
        future.addFirst(new EOSToken("}"));
        future.addFirst(new GroupedStatements(nodeList));
        return true;
    }
}
