package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class CostumeDeclaration extends VariableDeclaration implements ParserNode, Declaration  {
    private IdentifierToken name;
    private StringToken file;
    public CostumeDeclaration(IdentifierToken costumeName, StringToken file) {
        super(false, costumeName, new StringToken("\""+costumeName.getMatchedStr()+"\""));
        this.name = costumeName; this.file = file;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name};
    }

    public IdentifierToken getName() {
        return name;
    }

    public StringToken getLocation() {
        return file;
    }
    @Override
    public String toString() {
        return "Costume Declaration w/ name="+name.getMatchedStr() + "&file="+file.getValue();
    }
}
