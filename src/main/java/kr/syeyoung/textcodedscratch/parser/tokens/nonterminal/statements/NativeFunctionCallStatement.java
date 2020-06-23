package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.NativeFunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NativeFunctionCallStatement extends FunctionCallStatement implements NativeFunctionCall {
    private IdentifierToken identifierToken;
    private Expression[] parameters;
    private NativeFunctionDeclaration nativeFunctionDeclaration;

    public NativeFunctionCallStatement(IdentifierToken token, Expression[] expressions, NativeFunctionDeclaration nativeFunctionDeclaration) {
        super(token, expressions);
        this.identifierToken = token;
        this.parameters = expressions;
        this.nativeFunctionDeclaration = nativeFunctionDeclaration;
        if (nativeFunctionDeclaration.isReporter()) throw new ParsingGrammarException("Tried to call reporter expression in a statement");
    }

    public IdentifierToken getFunctionName() {
        return identifierToken;
    }
    public Expression[] getParameters() {
        return parameters;
    }

    public NativeFunctionDeclaration getFunction() {return nativeFunctionDeclaration;}

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] parserNodes = new ParserNode[parameters.length + 1];
        parserNodes[0] = identifierToken;
        System.arraycopy(parameters,0,parserNodes, 1, parameters.length);
        return parserNodes;
    }

    @Override
    public String toString() {
        return "{Native Function Call: "+identifierToken+", parameters: ["+ Arrays.asList(parameters).stream().map(expr -> expr.toString()).collect(Collectors.joining(", "))+"]}";
    }


    @Override
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String json = nativeFunctionDeclaration.json();
        if (nativeFunctionDeclaration.isReporter()) throw new ParsingGrammarException("native Reporter function used as a statement : " +getFunctionName().getMatchedStr());
        String id = builder.getNextID();
        Object[] asInputarrays = new Object[parameters.length];
        for (int i =0; i < parameters.length; i++)
            asInputarrays[i] = parameters[i].buildJSON(id, null, builder);
        Object[] asFieldarrays = new Object[parameters.length];
        for (int i =0; i < parameters.length; i++) {
            Object obj = asInputarrays[i];
            if (obj instanceof String) asFieldarrays[i] = null;
            else if (obj instanceof JSONArray) {
                asFieldarrays[i] = new JSONArray().put(((JSONArray) obj).get(1)).put(((JSONArray) obj).get(2));
            }
        }

        JSONObject obj = new JSONObject(json);
        fillParameters(obj, asInputarrays,asFieldarrays);
        builder.putComplexObject(id, obj);
        return id;
    }

    private void fillParameters(JSONObject obj, Object[] parameters, Object[] fieldParam) {
        for (String str: obj.keySet()) {
            Object obj2 = obj.get(str);
            if (obj2 instanceof JSONObject) fillParameters((JSONObject) obj2, parameters, fieldParam);
            else if (obj2 instanceof JSONArray) fillParameters((JSONArray) obj2, parameters, fieldParam);
            else if (obj2 instanceof String) {
                String content = (String) obj2;
                if (!content.startsWith("$TCS$")) continue;
                boolean isInput = content.charAt(5) == 'I';
                String index = content.substring(6);
                if (index.isEmpty()) continue;;
                int i;
                try {
                    i = Integer.parseInt(index);
                } catch (Exception e) {continue;}

                if (i >= parameters.length) continue;

                if (!isInput && fieldParam[i] == null) throw new ParsingGrammarException("Expression to where it was not expected");
                obj.put(str, isInput ? parameters[i] : fieldParam[i]);
            }
        }
    }
    private void fillParameters(JSONArray obj, Object[] parameters, Object[] fieldParam) {
        for (int i = 0; i < obj.length(); i++) {
            Object obj2 = obj.get(i);
            if (obj2 instanceof JSONObject) fillParameters((JSONObject) obj2, parameters, fieldParam);
            else if (obj2 instanceof JSONArray) fillParameters((JSONArray) obj2, parameters, fieldParam);
            else if (obj2 instanceof String) {
                String content = (String) obj2;
                if (!content.startsWith("$TCS$")) continue;
                boolean isInput = content.charAt(5) == 'I';
                String index = content.substring(6);
                if (index.isEmpty()) continue;;
                int j;
                try {
                    j = Integer.parseInt(index);
                } catch (Exception e) {continue;}

                if (j >= parameters.length) continue;

                if (!isInput && fieldParam[j] == null) throw new ParsingGrammarException("Expression to where it was not expected");
                obj.put(i, isInput ? parameters[j] : fieldParam[j]);
            }
        }
    }
}
