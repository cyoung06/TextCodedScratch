package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.Expression;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventDeclaration implements ParserNode, ScratchTransferable, Declaration {
    private IdentifierToken identifierToken;
    private GroupedStatements toExecute;
    private Expression optionalParameter;

    private NativeEventDeclaration eventJsonDeclaration;

    public EventDeclaration(IdentifierToken identifierToken, GroupedStatements inside) {
        this.identifierToken = identifierToken;
        this.toExecute = inside;
    }
    public EventDeclaration(IdentifierToken identifierToken, GroupedStatements inside, Expression optionalParameter) {
        this.identifierToken = identifierToken;
        this.toExecute = inside;
        this.optionalParameter = optionalParameter;
    }

    public void setEventJsonDeclaration(NativeEventDeclaration eventJsonDeclaration) {
        this.eventJsonDeclaration = eventJsonDeclaration;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {identifierToken, toExecute};
    }

    public IdentifierToken getEvent() {
        return identifierToken;
    }

    public GroupedStatements Statements() {
        return toExecute;
    }

    public Expression getOptionalParameter() {return optionalParameter;}
    @Override
    public String toString() {
        return "Event "+this.identifierToken+" with grouped statements "+this.toExecute;
    }



    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String json = eventJsonDeclaration.json();
        String id = builder.getNextID();

        JSONObject obj = new JSONObject(json);
        fillParameters(obj, id, builder);
        obj.put("parent", parentId == null ? JSONObject.NULL : parentId);
        builder.putComplexObject(id, obj);

        Object[] id2 = toExecute.buildJSON(id, nextId, builder);
        obj.put("next", id2[0] == null ? JSONObject.NULL : id2[0]);
        return new String[] {id, (String) id2[1]};
    }

    private void fillParameters(JSONObject obj, String id, ScriptBuilder builder) {
        for (String str: obj.keySet()) {
            Object obj2 = obj.get(str);
            if (obj2 instanceof JSONObject) fillParameters((JSONObject) obj2, id, builder);
            else if (obj2 instanceof JSONArray) fillParameters((JSONArray) obj2, id, builder);
            else if (obj2 instanceof String) {
                String prevContent = (String) obj2;
                Object FinalContent = obj2;

                if (prevContent.contains("$TCS$") && optionalParameter == null) throw new ParsingGrammarException("Optional Parameter Required for event "+identifierToken.getMatchedStr());

                if (prevContent.equals("$TCS$Expr$")) FinalContent = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                else if (prevContent.contains("$TCS$Const$")) {
                    Object json = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                    if (!(json instanceof JSONArray)) throw new ParsingGrammarException("Illegal Event argument for "+identifierToken.getMatchedStr());
                    FinalContent = prevContent.replace("$TCS$Const$", String.valueOf(((JSONArray) json).get(1)));
                } else if (prevContent.equals("$TCS$ConstExpr$")) {
                    Object json = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                    if (!(json instanceof JSONArray)) throw new ParsingGrammarException("Illegal Event argument for "+identifierToken.getMatchedStr());
                    FinalContent = json;
                }

                obj.put(str, FinalContent);
            }
        }
    }
    private void fillParameters(JSONArray obj, String id, ScriptBuilder builder) {
        for (int i = 0; i < obj.length(); i++) {
            Object obj2 = obj.get(i);
            if (obj2 instanceof JSONObject) fillParameters((JSONObject) obj2, id, builder);
            else if (obj2 instanceof JSONArray) fillParameters((JSONArray) obj2, id, builder);
            else if (obj2 instanceof String) {
                String prevContent = (String) obj2;
                Object FinalContent = obj2;

                if (prevContent.contains("$TCS$") && optionalParameter == null) throw new ParsingGrammarException("Optional Parameter Required for event "+identifierToken.getMatchedStr());

                if (prevContent.equals("$TCS$Expr$")) FinalContent = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                else if (prevContent.contains("$TCS$Const$")) {
                    Object json = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                    if (!(json instanceof JSONArray)) throw new ParsingGrammarException("Illegal Event argument for "+identifierToken.getMatchedStr());
                    FinalContent = prevContent.replace("$TCS$Const$", String.valueOf(((JSONArray) json).get(1)));
                } else if (prevContent.equals("$TCS$ConstExpr$")) {
                    Object json = optionalParameter.simplify().buildJSON(id, null, builder)[0];
                    if (!(json instanceof JSONArray)) throw new ParsingGrammarException("Illegal Event argument for "+identifierToken.getMatchedStr());
                    FinalContent = json;
                }

                obj.put(i, FinalContent);
            }
        }
    }
}
