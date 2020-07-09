package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;

public class AccessedIdentifier extends IdentifierToken {
    private IdentifierToken[] parents;
    private IdentifierToken identifier;

    public AccessedIdentifier(IdentifierToken parent, IdentifierToken identifier) {
        super((parent instanceof AccessedIdentifier ? ((AccessedIdentifier) parent).getIdentifierConcated() : parent.getMatchedStr())+"::"+identifier.getMatchedStr());
        this.parents = (parent instanceof AccessedIdentifier) ? (IdentifierToken[]) parent.getChildren() : new IdentifierToken[] {parent};
        this.identifier = identifier;
    }

    public IdentifierToken[] getParents() {
        return parents;
    }

    public IdentifierToken getIdentifier() {
        return identifier;
    }

    public String getIdentifierConcated() {
        StringBuilder builder = new StringBuilder();
        for (IdentifierToken token: parents) {
            builder.append(token.getMatchedStr()); builder.append("::");
        }
        builder.append(identifier.getMatchedStr());
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Accessed Identifier - " +getIdentifierConcated();
    }

    @Override
    public ParserNode[] getChildren() {
        IdentifierToken[] tokens = new IdentifierToken[parents.length + 1];
        System.arraycopy(parents, 0, tokens, 0, parents.length);
        tokens[parents.length] = identifier;
        return tokens;
    }
}
