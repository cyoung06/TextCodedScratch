package kr.syeyoung.textcodedscratch.parser.exception;

public class ParsingGrammarException extends RuntimeException {
    public ParsingGrammarException(String reason) {
        super(reason);
    }
}
