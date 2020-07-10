package kr.syeyoung.textcodedscratch.spritebuilder;

import java.io.File;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(file, resource.file) &&
                Objects.equals(hash, resource.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, hash);
    }
}
