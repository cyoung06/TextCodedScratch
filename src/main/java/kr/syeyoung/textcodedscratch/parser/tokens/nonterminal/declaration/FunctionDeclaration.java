package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.util.ScratchBlockBuilder;
import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FunctionDeclaration implements ParserNode, ScratchTransferable, Declaration {
    private IdentifierToken identifierToken;
    private FunctionParameter[] parameters;
    private GroupedStatements toExecute;

    public FunctionDeclaration(IdentifierToken identifierToken, FunctionParameter[] parameters, GroupedStatements inside) {
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
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String defID = builder.getNextID();
        String protoID = builder.getNextID();
        {
            JSONArray paramIDs = new JSONArray();
            JSONArray paramNames = new JSONArray();
            JSONArray defaults = new JSONArray();
            String procCode = identifierToken.getMatchedStr();
            ScratchBlockBuilder sbb = new ScratchBlockBuilder().op("procedures_prototype").nextId(null).parentId(defID).shadow(true).topLevel(false);
            for (int i =0; i < parameters.length; i++) {
                Object id = parameters[i].buildJSON(protoID, null, builder);
                paramNames.put(parameters[i].getName().getMatchedStr());
                defaults.put("");
                sbb.input("$TCS_FP$_"+identifierToken.getMatchedStr()+"$"+parameters[i].getName().getMatchedStr(), id);
                paramIDs.put("$TCS_FP$_"+identifierToken.getMatchedStr()+"$"+parameters[i].getName().getMatchedStr());
                procCode += " "+(parameters[i].getType() == FunctionParameter.ParameterType.TEXT ? "%s" : "%b");
            }
            sbb.put("mutation", new JSONObject().put("tagName", "mutation").put("children", new JSONArray()).put("proccode", procCode).put("argumentids", paramIDs.toString()).put("argumentnames", paramNames.toString()).put("argumentdefaults", defaults.toString()).put("warp", "false"));

            builder.putComplexObject(protoID, sbb.build());
        }
        builder.putComplexObject(defID, new ScratchBlockBuilder().op("procedures_definition").nextId(null).parentId(parentId).input("custom_block", new JSONArray().put(1).put(protoID)).shadow(false).topLevel(true).xy(0,0).build());

        Object id2 = toExecute.buildJSON(defID, null, builder);
        builder.getComplexObject(defID).put("next", id2 == null ? JSONObject.NULL : id2);

        String id3 = StackHelper.putStack(builder, (String) id2, nextId, new StringToken(""));
        builder.getComplexObject((String) id2).put("next", id3 == null ? JSONObject.NULL : id3);
        return id3;
    }

    @Override
    public String toString() {
        return "Function "+this.identifierToken+" with parameters ["+ Arrays.asList(parameters).stream().map(p -> p.toString()).collect(Collectors.joining(", "))+"] grouped statements "+this.toExecute;
    }
}
