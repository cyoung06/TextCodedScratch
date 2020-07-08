package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackAddingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FunctionExprCallMicroStatement implements Statements, FunctionCall, StackRequringOperation {
    private IdentifierToken identifierToken;
    private Expression[] parameters;
    private Statements stmt;

    private FunctionDeclaration functionDeclaration;

    public FunctionExprCallMicroStatement(IdentifierToken token, Expression[] expressions, Statements stmt) {
        this.identifierToken = token;
        this.stmt = stmt;
        this.parameters = expressions;
    }

    public IdentifierToken getFunctionName() {
        return identifierToken;
    }
    public Expression[] getParameters() {
        return parameters;
    }

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] parserNodes = new ParserNode[parameters.length + 1];
        parserNodes[0] = identifierToken;
        System.arraycopy(parameters,0,parserNodes, 1, parameters.length);
        return parserNodes;
    }

    public void setFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        this.functionDeclaration = functionDeclaration;
    }

    @Override
    public String toString() {
        return "{Function Call: "+identifierToken+", parameters: ["+ Arrays.asList(parameters).stream().map(expr -> expr.toString()).collect(Collectors.joining(", "))+"]}";
    }

    // {"tagName":"mutation","children":[],"proccode":"asdasd %s %s","argumentids":"[\"input1\",\"input2\"]","warp":"false"}}
    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id2 = builder.getNextID();

        ScratchBlockBuilder sbb = new ScratchBlockBuilder().op("procedures_call").nextId(nextId).parentId(parentId).shadow(false).topLevel(false);
        if (functionDeclaration == null) throw new ParsingGrammarException("No Function Declartion Found for "+identifierToken.getMatchedStr());
        FunctionParameter[] parametersDef = functionDeclaration.getParameters();
        String procCode = functionDeclaration.getName().getMatchedStr();
        JSONArray inputIDs = new JSONArray();
        for (int i =0; i<parametersDef.length; i++) {
            FunctionParameter parameter = parametersDef[i];
            procCode += " "+(parameter.getType() == FunctionParameter.ParameterType.TEXT ? "%s" : "%b");
            String id = "$TCS_FP$_"+functionDeclaration.getName().getMatchedStr()+"$"+parameter.getName().getMatchedStr();
            inputIDs.put(id);
            sbb.input(id, parameters[i].buildJSON(id2, null, builder));
        }
        sbb.put("mutation", new JSONObject().put("tagName", "mutation").put("children", new JSONArray()).put("proccode", procCode).put("argumentids", inputIDs.toString()));
        builder.putComplexObject(id2, sbb.build());
        return id2;
    }


    private int stack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.stack = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return stack;
    }
}
