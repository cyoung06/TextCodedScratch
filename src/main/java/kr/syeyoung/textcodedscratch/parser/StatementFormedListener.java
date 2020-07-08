package kr.syeyoung.textcodedscratch.parser;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;

import java.util.LinkedList;

public interface StatementFormedListener {
    public void process(Statements formed, ParserNode parent, LinkedList<ParserNode> past, LinkedList<ParserNode> future);
}
