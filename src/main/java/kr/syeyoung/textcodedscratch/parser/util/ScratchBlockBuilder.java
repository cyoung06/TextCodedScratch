package kr.syeyoung.textcodedscratch.parser.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScratchBlockBuilder {
    JSONObject scratchBlock = new JSONObject();
    public ScratchBlockBuilder() {
        scratchBlock.put("inputs", new JSONObject());
        scratchBlock.put("fields", new JSONObject());
    };

    public ScratchBlockBuilder parentId(String parentId) {scratchBlock.put("parent", parentId == null ? JSONObject.NULL : parentId); return this;}
    public ScratchBlockBuilder nextId(String nextId) {scratchBlock.put("next", nextId == null ? JSONObject.NULL : nextId); return this;}
    public ScratchBlockBuilder op(String opcode) {scratchBlock.put("opcode", opcode == null ? JSONObject.NULL : opcode); return this;}


    public ScratchBlockBuilder input(String name, Object array) {
        JSONObject obj = scratchBlock.optJSONObject("inputs");
        scratchBlock.put("inputs", obj.append(name, 1).append(name, array));
        return this;
    }

    public ScratchBlockBuilder field(String name, JSONArray array) {
        JSONObject obj = scratchBlock.optJSONObject("fields");
        scratchBlock.put("fields", obj.put(name, array));
        return this;
    }
    public ScratchBlockBuilder shadow(boolean shadow) {scratchBlock.put("shadow", shadow); return this;}
    public ScratchBlockBuilder topLevel(boolean topLevel) {scratchBlock.put("topLevel", topLevel); return this;}
    public ScratchBlockBuilder xy(int x, int y) {scratchBlock.put("x", x).put("y",y); return this;}

    public ScratchBlockBuilder put(String id, Object value) {
        scratchBlock.put(id, value  == null ? JSONObject.NULL : value);
        return this;
    }

    public JSONObject build() {return scratchBlock;}
}
