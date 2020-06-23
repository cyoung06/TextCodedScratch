package kr.syeyoung.textcodedscratch.parser;

import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;

public interface ScratchTransferable {
    public Object buildJSON(String parentId, String nextId, ScriptBuilder builder);
}
