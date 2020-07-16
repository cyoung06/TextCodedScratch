package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.VariableDeclaration;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import org.json.JSONArray;
import org.json.JSONObject;

public class FunctionParameter extends VariableDeclaration implements ParserNode, ScratchTransferable {
    private IdentifierToken name;
    private ParameterType type;

    public FunctionParameter(IdentifierToken name, ParameterType type) {
        super(false, name, null);
        this.name = name;
        this.type = type;
    }

    @Override
    public ParserNode[] getChildren() {
        return new ParserNode[] {name, type};
    }

    public IdentifierToken getName() {
        return name;
    }

    public ParameterType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{Parameter "+name+": "+type+"}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String ID = builder.putComplexObject(new ScratchBlockBuilder().op(type == ParameterType.TEXT ? "argument_reporter_string_number" : "argument_reporter_boolean").nextId(nextId).parentId(parentId).field("VALUE", new JSONArray().put(name.getMatchedStr()).put(JSONObject.NULL)).shadow(true).topLevel(false).build());
        return new String[] {ID, ID};
    }

    public static enum ParameterType implements ParserNode {
        TEXT("Text"), BOOLEAN("Boolean"), VARIABLE_POINTER("VariablePointer");

        private String capitalized;
        private ParameterType(String capitalized) {
            this.capitalized = capitalized;
        }

        public String getCapitalized() {
            return capitalized;
        }

        @Override
        public ParserNode[] getChildren() {
            return new ParserNode[0];
        }


    }
}
