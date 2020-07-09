package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;

public interface Statements extends ParserNode, ScratchTransferable, StackRequringOperation {

    public int getStackCountAtExecution();
}
