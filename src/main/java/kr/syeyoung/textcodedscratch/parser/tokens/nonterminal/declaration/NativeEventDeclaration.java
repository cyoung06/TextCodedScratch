package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

public class NativeEventDeclaration implements ParserNode, ScratchTransferable, Declaration {
    private IdentifierToken identifierToken;
    private StringToken json;

    public NativeEventDeclaration(IdentifierToken identifierToken,StringToken inside) {
        this.identifierToken = identifierToken;
        this.json = inside;
    }
    public IdentifierToken getName() {
        return identifierToken;
    }

    public String json() {return json.getValue(String.class);}

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] nodes = new ParserNode[2];
        nodes[0] = identifierToken;
        nodes[1] = json;
        return nodes;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return null;
    }

    @Override
    public String toString() {
        return "Native Event "+this.identifierToken+" with json "+this.json;
    }
}
