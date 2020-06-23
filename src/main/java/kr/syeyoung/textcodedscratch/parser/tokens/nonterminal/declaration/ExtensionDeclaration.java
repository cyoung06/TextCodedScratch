package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class ExtensionDeclaration implements ParserNode, Declaration {
    private StringToken name;
    public ExtensionDeclaration(StringToken extensionName) {
        this.name = extensionName;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    @Override
    public String toString() {
        return "Extension Declaration w/ name="+name.getValue();
    }
}
