package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class SoundDeclaration extends VariableDeclaration implements ParserNode, Declaration {
    private IdentifierToken name;
    private StringToken file;
    public SoundDeclaration(IdentifierToken soundName, StringToken file) {
        super(false, soundName, new StringToken("\""+soundName.getMatchedStr()+"\""));
        this.name = soundName; this.file = file;
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
