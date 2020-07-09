package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.BooleanToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NativeFunctionDeclaration extends FunctionDeclaration implements ParserNode, ScratchTransferable, Declaration {
    private IdentifierToken identifierToken;
    private FunctionParameter[] parameters;
    private StringToken json;
    private boolean isReporter;

    public NativeFunctionDeclaration(IdentifierToken identifierToken, FunctionParameter[] parameters, StringToken inside, boolean isReporter) {
        super(identifierToken, parameters, new GroupedStatements(new Statements[0]), false);
        this.identifierToken = identifierToken;
        this.parameters = parameters;
        this.json = inside;
        this.isReporter= isReporter;
    }
    public IdentifierToken getName() {
        return identifierToken;
    }

    public FunctionParameter[] getParameters() {
        return parameters;
    }

    public boolean isReporter() {return isReporter;}

    public String json() {return json.getValue(String.class);}

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] nodes = new ParserNode[parameters.length + 3];
        nodes[0] = identifierToken;
        System.arraycopy(parameters, 0, nodes, 1, parameters.length);
        nodes[nodes.length - 2] = json;
        nodes[nodes.length - 1] = new BooleanToken(isReporter);

        return nodes;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        return null;
    }

    @Override
    public String toString() {
        return "Native "+(isReporter ? "REPORTER" : "")+"Function "+this.identifierToken+" with parameters ["+ Arrays.asList(parameters).stream().map(p -> p.toString()).collect(Collectors.joining(", "))+"] json "+this.json;
    }
}
