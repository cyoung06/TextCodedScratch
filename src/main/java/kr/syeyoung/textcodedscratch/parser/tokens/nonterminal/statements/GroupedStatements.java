package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GroupedStatements implements ParserNode, Statements {
    private Statements[] statements;
    public GroupedStatements(Statements[] statements) {
        this.statements = statements;
    }

    @Override
    public ParserNode[] getChildren() {
        return statements;
    }

    @Override
    public String toString() {
        return "{Grouped Statements["+Arrays.asList(statements).stream().map(st -> st == null ? "null" : st.toString()).collect(Collectors.joining(", "))+"]}";
    }

    @Override
    public Object[] buildJSON(String rparentId, String rnextId, ScriptBuilder builder) {
        String firstId = null;
        String[] prevID = new String[] {rparentId, rparentId};
        JSONObject prevObj = null;
        for (int i = 0; i < statements.length; i++) {
            Object[] preprev = statements[i].buildJSON(prevID[1], null, builder);
            if (preprev == null) continue;
            prevID = (String[]) preprev;
            if (firstId == null)
                firstId = prevID[1];
            if (prevObj != null) prevObj.put("next", prevID[0]);
            prevObj = builder.getComplexObject(prevID[1]);
        }
        if (prevObj != null)
        prevObj.put("next", rnextId == null ? JSONObject.NULL : rnextId);

        return new String[] {firstId, prevID[1]};
    }
}
