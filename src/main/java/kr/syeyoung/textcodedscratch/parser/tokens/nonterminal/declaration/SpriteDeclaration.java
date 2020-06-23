package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class SpriteDeclaration implements ParserNode, Declaration {
    private StringToken name;
    public SpriteDeclaration(StringToken spriteName) {
        this.name = spriteName;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    @Override
    public String toString() {
        return "Sprite Declaration w/ name="+name.getValue();
    }

    public StringToken getName() {
        return name;
    }
}
