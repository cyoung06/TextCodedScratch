package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class RequireDeclaration implements ParserNode, Declaration {
    private StringToken name;
    public RequireDeclaration(StringToken extensionName) {
        this.name = extensionName;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }


    public StringToken getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Require Declaration w/ name="+name.getValue();
    }
}
