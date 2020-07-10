package kr.syeyoung.textcodedscratch;

import kr.syeyoung.textcodedscratch.parser.Parser;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.CostumeDeclaration;
import kr.syeyoung.textcodedscratch.parser.Tokenizer;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.SoundDeclaration;
import kr.syeyoung.textcodedscratch.spritebuilder.FileUtils;
import kr.syeyoung.textcodedscratch.spritebuilder.SB3ProjectBuilder;
import kr.syeyoung.textcodedscratch.spritebuilder.SpriteTargetBuilder;
import sun.security.pkcs.ParsingException;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
        if (args.length == 0) {
            System.out.println("Correct Usage: java -jar (filename).jar [TCS Sprite Files Dir]");
            return;
        }
        File f = new File(args[0]);
        if (f.isDirectory()) {
            List<SpriteDefinition> stbs = new ArrayList<>();
            boolean stageFound = false;
            for (File file: f.listFiles()) {
                if (!file.getName().endsWith(".tcs")) continue;

                FileInputStream fis = new FileInputStream(args[0]);
                Tokenizer tokenizer = new Tokenizer(fis);
                tokenizer.Tokenize();
                Parser parser = new Parser(new LinkedList<>(tokenizer.getTerminalNodes().stream().map(t -> (ParserNode)t).collect(Collectors.toList())), file);
                parser.parse();
                SpriteDefinition definition = parser.getSyntexCheckerRule().getDefinition();
                stbs.add(definition);
            }

            SB3ProjectBuilder sb3ProjectBuilder = new SB3ProjectBuilder(stbs);
            sb3ProjectBuilder.buildToFile(new File("project.sb3"));
        } else {
            FileInputStream fis = new FileInputStream(args[0]);
            Tokenizer tokenizer = new Tokenizer(fis);
            tokenizer.Tokenize();
            Parser parser = new Parser(new LinkedList<>(tokenizer.getTerminalNodes().stream().map(t -> (ParserNode)t).collect(Collectors.toList())), f);
            parser.parse();
            SpriteDefinition definition = parser.getSyntexCheckerRule().getDefinition();
            SpriteTargetBuilder stb = new SpriteTargetBuilder(definition);

            String fileName = new File(args[0]).getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            stb.buildToFile(new File(fileName + ".sprite3"));
        }
    }
}
