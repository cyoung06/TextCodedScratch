package kr.syeyoung.textcodedscratch.parser.tokens.terminal;

public class EOFToken extends EOSToken {
    public EOFToken() {
        super("\0");
    }
}
