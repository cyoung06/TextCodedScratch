package kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant;

import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TerminalNode;

public interface ConstantNode extends TerminalNode, ScratchTransferable, Expression {
    public Object getValue();

    public <T> T getValue(Class<T> tClass);
}
