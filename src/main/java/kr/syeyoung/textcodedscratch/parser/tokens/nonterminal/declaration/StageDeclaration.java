package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class StageDeclaration extends SpriteDeclaration implements ParserNode, Declaration {
    private StringToken name;
    public StageDeclaration() {
        super(new StringToken("\"Stage\""));
        this.name = super.getName();
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    @Override
    public String toString() {
        return "Stage Declaration";
    }

    public StringToken getName() {
        return name;
    }
}
