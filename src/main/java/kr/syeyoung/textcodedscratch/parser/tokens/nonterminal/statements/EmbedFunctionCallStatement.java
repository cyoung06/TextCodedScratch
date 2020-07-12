package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.NativeFunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.EmbedFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.FunctionVariableExpression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EmbedFunctionCallStatement extends FunctionCallStatement {
    private IdentifierToken identifierToken;
    private Expression[] parameters;
    private EmbedFunctionDeclaration embedFunctionDeclaration;

    public EmbedFunctionCallStatement(IdentifierToken token, Expression[] expressions, EmbedFunctionDeclaration nativeFunctionDeclaration) {
        super(token, expressions);
        this.identifierToken = token;
        this.parameters = expressions;
        this.embedFunctionDeclaration = nativeFunctionDeclaration;
    }

    public IdentifierToken getFunctionName() {
        return identifierToken;
    }
    public Expression[] getParameters() {
        return parameters;
    }

    public EmbedFunctionDeclaration getFunction() {return embedFunctionDeclaration;}

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] parserNodes = new ParserNode[parameters.length + 1];
        parserNodes[0] = identifierToken;
        System.arraycopy(parameters,0,parserNodes, 1, parameters.length);
        return parserNodes;
    }

    @Override
    public String toString() {
        return "{Embed Function Call: "+identifierToken+", parameters: ["+ Arrays.asList(parameters).stream().map(expr -> expr.toString()).collect(Collectors.joining(", "))+"]}";
    }


    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        Map<String, Expression> parameterMap = buildParameterMap();
        GroupedStatements gs = embedFunctionDeclaration.getStatemnts();
        recursivelyReplaceParameters(parameterMap, gs);

        return gs.buildJSON(parentId, nextId, builder);
    }

    private void recursivelyReplaceParameters(Map<String, Expression> parameterMap, ParserNode node) {
        if (node instanceof FunctionVariableExpression) {
            ((FunctionVariableExpression) node).setProxyExpr(parameterMap.get(((FunctionVariableExpression) node).getVariableName().getMatchedStr()));
        }
        for (ParserNode pn:node.getChildren()) {
            recursivelyReplaceParameters(parameterMap, pn);
        }
    }

    private Map<String, Expression> buildParameterMap() {
        Map<String, Expression> parameterMap = new HashMap<>();
        FunctionParameter[] functionParameters = embedFunctionDeclaration.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            parameterMap.put(functionParameters[i].getName().getMatchedStr(), parameters[i]);
        }
        return parameterMap;
    }
}
