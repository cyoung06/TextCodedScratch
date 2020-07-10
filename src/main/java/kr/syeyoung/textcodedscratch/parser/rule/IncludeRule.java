package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.Main;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.Tokenizer;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.EOSToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.TerminalNode;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordModule;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.KeywordStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IncludeRule implements ParserRule {
    private File f;
    public IncludeRule(File f) {
        this.f = f;
    }
    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        if (past.getLast() instanceof RequireDeclaration) {
            RequireDeclaration rdec = (RequireDeclaration) past.removeLast();
            try {
                String name = rdec.getName().getValue(String.class) + ".tcsmodule";

                InputStream possible = IncludeRule.class.getResourceAsStream("/kr/syeyoung/textcodedscratch/predefined/"+name);
                if (possible == null) possible = new FileInputStream(new File(f, name));

                Tokenizer tokenizer = new Tokenizer(possible);
                ParserNode pn = tokenizer.getNextToken();
                if (!(pn instanceof KeywordModule)) throw new ParsingGrammarException("File referenced in Require Declaration should be a module file - " + rdec.getName().getValue(String.class));
                tokenizer.Tokenize();

                LinkedList<TerminalNode> theList = tokenizer.getTerminalNodes();
                theList.removeFirst();
                theList.removeFirst();

                future.addAll(0, theList);
                future.addFirst(new EOSToken("INCLUDE"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
