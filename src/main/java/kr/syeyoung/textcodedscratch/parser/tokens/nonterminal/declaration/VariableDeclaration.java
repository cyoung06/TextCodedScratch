package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;

public class VariableDeclaration implements ParserNode, Declaration {
    private boolean isGlobal;
    private IdentifierToken name;
    private ConstantNode defaultValue;
    public VariableDeclaration(boolean isGlobal, IdentifierToken name, ConstantNode defaultValue) {
        this.isGlobal = isGlobal; this.name = name; this.defaultValue = defaultValue;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public IdentifierToken getName() {
        return name;
    }

    public ConstantNode getDefaultValue() {
        return defaultValue;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    @Override
    public String toString() {
        return "Variable Declaration w/ name="+name.getMatchedStr() + "&value="+defaultValue.getValue()+"&global="+isGlobal;
    }
}
