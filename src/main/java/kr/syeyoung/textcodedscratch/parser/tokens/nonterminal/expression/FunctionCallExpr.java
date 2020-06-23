package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FunctionCallExpr implements Expression, FunctionCall {
    private IdentifierToken identifierToken;
    private Expression[] parameters;
    private FunctionDeclaration functionDeclaration;

    public FunctionCallExpr(IdentifierToken token, Expression[] expressions) {
        this.identifierToken = token;
        this.parameters = expressions;
    }

    public IdentifierToken getFunctionName() {
        return identifierToken;
    }
    public Expression[] getParameters() {
        return parameters;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    public void setFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        this.functionDeclaration = functionDeclaration;
    }

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] parserNodes = new ParserNode[parameters.length + 1];
        parserNodes[0] = identifierToken;
        System.arraycopy(parameters,0,parserNodes, 1, parameters.length);
        return parserNodes;
    }

    @Override
    public String toString() {
        return "{Function Call: "+identifierToken+", parameters: ["+ Arrays.asList(parameters).stream().map(expr -> expr.toString()).collect(Collectors.joining(", "))+"]}";
    }

    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {

        throw new AssertionError("Not implemented");
    }
}
