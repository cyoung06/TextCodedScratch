package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StackDeclaration extends ListDeclaration {
    public StackDeclaration(IdentifierToken soundName) {
        super(false, soundName, new ConstantNode[0]);
    }

    @Override
    public String toString() {
        return "Stack Declaration w/ name="+getName();
    }
}
