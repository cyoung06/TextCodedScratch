package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class ModuleDeclaration implements ParserNode, Declaration {

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[0];
    }

    @Override
    public String toString() {
        return "Module Declaration";
    }
}
