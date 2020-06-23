package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ListDeclaration implements ParserNode, Declaration {
    private boolean isGlobal;
    private IdentifierToken name;
    private ConstantNode[] defaultValues;
    public ListDeclaration(boolean isGlobal, IdentifierToken soundName, ConstantNode[] defaultValues) {
        this.isGlobal = isGlobal; this.name = soundName; this.defaultValues = defaultValues;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public IdentifierToken getName() {
        return name;
    }

    public ConstantNode[] getDefaultValues() {
        return defaultValues;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    @Override
    public String toString() {
        return "List Declaration w/ name="+name.getMatchedStr() + "&value=["+ Arrays.asList(defaultValues).stream().map(a -> a.getValue().toString()).collect(Collectors.joining(", "))+"]&global="+isGlobal;
    }
}
