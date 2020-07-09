package kr.syeyoung.textcodedscratch.spritebuilder;

import java.io.File;

public class Resource {
    public Resource(File file, String hash) {
        this.file = file;
        this.hash = hash;
    }

    private File file;
    private String hash;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
