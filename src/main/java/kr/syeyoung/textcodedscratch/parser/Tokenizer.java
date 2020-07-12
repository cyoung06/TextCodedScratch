package kr.syeyoung.textcodedscratch.parser;

import kr.syeyoung.textcodedscratch.exception.TokenizerException;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.BooleanToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.keywords.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Tokenizer {
    private InputStream stream;
    private LinkedList<TerminalNode> terminalNodes = new LinkedList<TerminalNode>();

    private static final Map<BiPredicate<String, Character>, Function<String, TerminalNode>> terminalNodeConverter = new LinkedHashMap<>();

    public Tokenizer(InputStream stream) {
        this.stream = new BufferedInputStream(stream);
    }

    public TerminalNode getNextToken() throws IOException {
        if (terminalNodes.size() == 0) {
            terminalNodes.add(new SOFToken());
        }
        if (!hasNextToken()) return terminalNodes.getLast();
        if (stream.available() <= 0) {
            terminalNodes.add(new EOFToken());
            return terminalNodes.peek();
        }
        int i;
        StringBuilder builder = new StringBuilder();
        while ((i = stream.read()) != -1) {
            char c = (char)i;
            builder.append(c);
            String str = sepecialTrimFront(builder.toString(), "\t\r ");
            stream.mark(1);
            int read = stream.read();
            stream.reset();
            Character next = read != -1 ? (char)read : null;
            for (Map.Entry<BiPredicate<String, Character>, Function<String, TerminalNode>> entry : terminalNodeConverter.entrySet()) {
                if (entry.getKey().test(str,next)) {
                    TerminalNode node = entry.getValue().apply(str);
                    terminalNodes.add(node);
                    return node;
                }
            }
        }

        throw new TokenizerException("no matching token found :: "+builder.toString());
    }

    public boolean hasNextToken() {
        return terminalNodes.isEmpty() || !(terminalNodes.getLast() instanceof EOFToken);
    }

    public void Tokenize() throws IOException {
        while(hasNextToken()) getNextToken();
    }

    public LinkedList<TerminalNode> getTerminalNodes() {
        return terminalNodes;
    }

    private static String sepecialTrimFront(String str, String toTrim) {
        int startIndex = 0;
        while (startIndex < str.length()) {
            if (toTrim.contains(String.valueOf(str.charAt(startIndex)))) {
                startIndex ++;
            } else {
                break;
            }
        }
        return (str.substring(startIndex));
    }


    private static final Pattern number = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern identifier = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_\\-]*");
    private static final Pattern identAfter = Pattern.compile("[a-zA-Z0-9_\\-]");

    private static final Pattern string = Pattern.compile("(\".*[^\\\\](?:[\\\\]{2})*\"|\"\")");
    static {
        terminalNodeConverter.put((str,next) -> str.equals("\n"), str -> new EOLToken(str));
        terminalNodeConverter.put((str,next) -> str.equals(";"), str -> new EOSToken(str));
        // CONSTANTS
        terminalNodeConverter.put((str,next) -> string.matcher(str).matches(), str -> new StringToken(str.replaceAll("\\\\(.)", "$1")));
        terminalNodeConverter.put((str,next) -> str.equals("true") || str.equals("false"), str -> new BooleanToken(str));
        terminalNodeConverter.put((str,next) -> number.matcher(str).matches() && (next < '0' || next > '9') && next != '.', str -> new NumberToken(str));
        // KEYWORDS
        terminalNodeConverter.put((str,next) -> str.equals("Var") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordVar());
        terminalNodeConverter.put((str,next) -> str.equals("Global") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordGlobal());
        terminalNodeConverter.put((str,next) -> str.equals("List") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordList());
        terminalNodeConverter.put((str,next) -> str.equals("Require") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordRequire());
        terminalNodeConverter.put((str,next) -> str.equals("Sprite") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordSprite());
        terminalNodeConverter.put((str,next) -> str.equals("Costume") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordCostume());
        terminalNodeConverter.put((str,next) -> str.equals("Sound") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordSound());
        terminalNodeConverter.put((str,next) -> str.equals("Event") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordEvent());
        terminalNodeConverter.put((str,next) -> str.equals("Func") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordFunc());
        terminalNodeConverter.put((str,next) -> str.equals("Boolean")  && String.valueOf(next).matches("\\W"), (str) -> new KeywordBoolean());
        terminalNodeConverter.put((str,next) -> str.equals("Text") && String.valueOf(next).matches("\\W"), (str) -> new KeywordText());
        terminalNodeConverter.put((str,next) -> str.equals("VariablePointer") && String.valueOf(next).matches("\\W"), (str) -> new KeywordVarPointer());


        terminalNodeConverter.put((str,next) -> str.equals("Extension") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordExtension());
        terminalNodeConverter.put((str,next) -> str.equals("Native") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordNative());
        terminalNodeConverter.put((str,next) -> str.equals("Embed") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordEmbed());
        terminalNodeConverter.put((str,next) -> str.equals("NoRefresh") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordNoRefresh());
        terminalNodeConverter.put((str,next) -> str.equals("Reporter") && " \t\n\r".contains(String.valueOf(next)), (str) -> new KeywordReporter());
        terminalNodeConverter.put((str,next) -> str.equals("Module") && " \t\n\r;".contains(String.valueOf(next)), (str) -> new KeywordModule());
        terminalNodeConverter.put((str,next) -> str.equals("Stage") && " \t\n\r;".contains(String.valueOf(next)), (str) -> new KeywordStage());




        // INTEGER OPERATIONS
        terminalNodeConverter.put((str,next) -> str.equals("+"), (str) -> new OperatorPlus());
        terminalNodeConverter.put((str,next) -> str.equals("-"), (str) -> new OperatorMinus());
        terminalNodeConverter.put((str,next) -> str.equals("*"), (str) -> new OperatorMulitply());
        terminalNodeConverter.put((str,next) -> str.equals("/") && (next == null || next != '/'), (str) -> new OperatorDivide());
        terminalNodeConverter.put((str,next) -> str.equals("%"), (str) -> new OperatorModular());
        // BOOL OPERATIONS
        terminalNodeConverter.put((str,next) -> str.equals("&&"), (str) -> new OperatorAnd());
        terminalNodeConverter.put((str,next) -> str.equals("||"), (str) -> new OperatorOr());
        terminalNodeConverter.put((str,next) -> str.equals("!"), (str) -> new OperatorNot());
        // POINTERISH OPERATION
        terminalNodeConverter.put((str,next) -> str.equals("&"), (str) -> new OperatorGetName());
        // COMPARISON
        terminalNodeConverter.put((str,next) -> str.equals(">"), (str) -> new OperatorBigger());
        terminalNodeConverter.put((str,next) -> str.equals("<"), (str) -> new OperatorLesser());
        terminalNodeConverter.put((str,next) -> str.equals("=="), (str) -> new OperatorEquals());
        // STRING OPERATION
        terminalNodeConverter.put((str,next) -> str.equals(".."), (str) -> new OperatorConcat());

        // SET
        terminalNodeConverter.put((str,next) -> str.equals("=") && next != '=', (str) -> new OperatorSet());

        // BRANCHES
        terminalNodeConverter.put((str,next) -> str.equals("if") && " \t\n\r".contains(String.valueOf(next)), str -> new KeywordIf());
        terminalNodeConverter.put((str,next) -> str.equals("else") && " \t\n\r".contains(String.valueOf(next)), str -> new KeywordElse());
        terminalNodeConverter.put((str,next) -> str.equals("while") && " \t\n\r".contains(String.valueOf(next)), str -> new KeywordWhile());
        terminalNodeConverter.put((str,next) -> str.equals("repeat") && " \t\n\r".contains(String.valueOf(next)), str -> new KeywordRepeat());
        terminalNodeConverter.put((str,next) -> str.equals("return") && " \t\n\r;".contains(String.valueOf(next)), str -> new KeywordReturn());

        // "IDENTIFIER"
        terminalNodeConverter.put((str,next) -> identifier.matcher(str).matches() && !identAfter.matcher(String.valueOf(next)).matches(), (str) -> new IdentifierToken(str));

        // BRACKETS!
        terminalNodeConverter.put((str,next) -> str.equals("("), str -> new POpenToken(str));
        terminalNodeConverter.put((str,next) -> str.equals(")"), str -> new PCloseToken(str));
        terminalNodeConverter.put((str,next) -> str.equals("{"), str -> new CBOpenToken(str));
        terminalNodeConverter.put((str,next) -> str.equals("}"), str -> new CBCloseToken(str));
        terminalNodeConverter.put((str,next) -> str.equals("["), str -> new SBOpenToken(str));
        terminalNodeConverter.put((str,next) -> str.equals("]"), str -> new SBCloseToken(str));

        // TOKENS
        terminalNodeConverter.put((str, next) -> str.equals(","), str -> new CommaToken(str));

        // COMMENT
        terminalNodeConverter.put((str,next) -> str.startsWith("//") && (next == null || next == '\n'), str -> new CommentToken(str));

        // ACCESSOR
        terminalNodeConverter.put((str, next) -> str.equals("::"), str -> new AccesorToken());
        terminalNodeConverter.put((str, next) -> str.equals(":") && next != ':', str -> new TypeToken());
    }
}
