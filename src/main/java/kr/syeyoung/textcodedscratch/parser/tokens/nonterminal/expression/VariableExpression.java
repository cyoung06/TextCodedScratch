package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.ICodeContextConsumer;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.SpriteDeclaration;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;

public class VariableExpression implements Expression, ICodeContextConsumer {
    private IdentifierToken variableName;
    private boolean isList = false;
    private boolean isGlobal = false;
    public VariableExpression(IdentifierToken variableName) {
        this.variableName = variableName;
    }
    public VariableExpression(IdentifierToken variableName, boolean list, boolean global) {
        this.variableName = variableName;
        this.isList = list;
        this.isGlobal = global;
    }
    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {variableName};
    }

    public IdentifierToken getVariableName() {
        return variableName;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return "{VAR: "+variableName+"}";
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {

        JSONArray arr =  new JSONArray().put(isList ? 13 : 12).put(variableName.getMatchedStr()).put("$TCS_"+(isList ? "L": "V")+"$"+(isGlobal ? "Stage" : icc.getSpriteName().getName().getValue())+"$"+variableName.getMatchedStr());
        return new Object[] {arr, arr};
    }


    private SpriteDefinition icc;
    @Override
    public void setICodeContext(ICodeContext context) {
        while (!(context instanceof SpriteDefinition)) context = context.getParent();
        icc = (SpriteDefinition) context;
    }

    public SpriteDefinition getSpriteDefinition() {
        return icc;
    }
}
