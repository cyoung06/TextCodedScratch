package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EmbedFunctionDeclaration extends FunctionDeclaration implements ParserNode, ScratchTransferable, Declaration {
    private IdentifierToken identifierToken;
    private FunctionParameter[] parameters;
    private GroupedStatements toExecute;

    public EmbedFunctionDeclaration(IdentifierToken identifierToken, FunctionParameter[] parameters, GroupedStatements inside) {
        super(identifierToken, parameters, inside, false);
        this.identifierToken = identifierToken;
        this.parameters = parameters;
        this.toExecute = inside;
    }
    public IdentifierToken getName() {
        return identifierToken;
    }

    public FunctionParameter[] getParameters() {
        return parameters;
    }

    public GroupedStatements getStatemnts() {
        return toExecute;
    }

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] nodes = new ParserNode[parameters.length + 2];
        nodes[0] = identifierToken;
        System.arraycopy(parameters, 0, nodes, 1, parameters.length);
        nodes[nodes.length - 1] = toExecute;

        return nodes;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return null;
    }

    @Override
    public String toString() {
        return "Embed Function "+this.identifierToken+" with parameters ["+ Arrays.asList(parameters).stream().map(p -> p.toString()).collect(Collectors.joining(", "))+"] grouped statements "+this.toExecute;
    }
}
