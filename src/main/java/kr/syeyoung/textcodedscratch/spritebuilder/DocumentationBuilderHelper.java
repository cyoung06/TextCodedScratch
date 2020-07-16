package kr.syeyoung.textcodedscratch.spritebuilder;

import kr.syeyoung.textcodedscratch.parser.Parser;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.Tokenizer;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeEventDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Native;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DocumentationBuilderHelper {
    public static void main(String[] args) throws IOException {
        String moduleName = "Video";
        String tcs = "Sprite \"sprite1\"\nRequire "+moduleName+";\nCostume costume1=\"default.png\"";

        ByteArrayInputStream bais = new ByteArrayInputStream(tcs.getBytes());

        Tokenizer tokenizer = new Tokenizer(bais);
        tokenizer.Tokenize();
        Parser parser = new Parser(new LinkedList<>(tokenizer.getTerminalNodes().stream().map(t -> (ParserNode)t).collect(Collectors.toList())), new File("doesitexist.txt"));
        parser.parse();
        SpriteDefinition definition = parser.getSyntexCheckerRule().getDefinition();

        HashMap<String, NativeEventDeclaration> events = definition.getEventsDefined();
        HashMap<String, FunctionDeclaration> functions = definition.getFunctions();

        String defaultMD = "# "+moduleName+" Module\n\n"+moduleName+" module can be imported via \n\n```\nRequire \""+moduleName+"\";\n```\n\n";

        String functionsMD = "## Defined Functions\n\n";
        if (functions.size() == 0)
            functionsMD += "N/A\n\n";

        for (FunctionDeclaration functionDeclaration:functions.values()) {
            String functionSignature = functionDeclaration.getName().getMatchedStr();
            functionSignature += "(";

            functionSignature += Arrays.asList(functionDeclaration.getParameters()).stream().map(fp -> fp.getName().getMatchedStr() + ": "+fp.getType().getCapitalized()).collect(Collectors.joining(", "));
            functionSignature += ")";

            functionsMD += "`"+functionSignature+"` : "+(functionDeclaration instanceof NativeFunctionDeclaration && ((NativeFunctionDeclaration) functionDeclaration).isReporter() ? "(Expression only)" :"") +"\n\n";
        }


        String eventsMD = "## Defined Events\n\n";
        if (events.size() == 0)
            eventsMD += "N/A\n\n";

        for (NativeEventDeclaration ned:events.values()) {
            String later = ned.json().contains("$TCS$Expr$") ? "(Requires expression/constant/variable parameter)" : ned.json().contains("$TCS$Const$") ? "(Requires constant parameter)" : "";
            eventsMD += "`"+ned.getName().getMatchedStr()+"` : "+later+"\n\n";
        }

        System.out.println(defaultMD + functionsMD + eventsMD);
    }
}