package kr.syeyoung.textcodedscratch.parser.util;

import org.json.JSONObject;

public class ScriptBuilder {
    private JSONObject script = new JSONObject();

    private long scriptCounter = 100;

    public String putComplexObject(JSONObject obj) {
        String id = getNextID();
        return putComplexObject(id, obj);
    }
    public String putComplexObject(String id,JSONObject obj) {
        script.put(id, obj);
        return id;
    }

    public JSONObject getComplexObject(String id) {
        return script.optJSONObject(id);
    }

    public String getNextID() {
        return Long.toString(scriptCounter++, 16);
    }

    public JSONObject getJSON() {
        return script;
    }
}
