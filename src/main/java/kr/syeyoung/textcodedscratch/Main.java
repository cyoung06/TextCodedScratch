package kr.syeyoung.textcodedscratch;

import kr.syeyoung.textcodedscratch.parser.Parser;
import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.CostumeDeclaration;
import kr.syeyoung.textcodedscratch.parser.Tokenizer;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.SoundDeclaration;
import kr.syeyoung.textcodedscratch.spritebuilder.FileUtils;
import kr.syeyoung.textcodedscratch.spritebuilder.SpriteTargetBuilder;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
        if (args.length == 0) {
            System.out.println("Correct Usage: java -jar (filename).jar [TCS Sprite File]");
            return;
        }

        FileInputStream fis = new FileInputStream(args[0]);
        Tokenizer tokenizer = new Tokenizer(fis);
        tokenizer.Tokenize();
        Parser parser = new Parser(new LinkedList<>(tokenizer.getTerminalNodes().stream().map(t -> (ParserNode)t).collect(Collectors.toList())));
        parser.parse();
        SpriteDefinition definition = parser.getSyntexCheckerRule().getDefinition();
        SpriteTargetBuilder stb = new SpriteTargetBuilder(definition);

        String fileName = new File(args[0]).getName();
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        stb.buildToFile(new File(fileName + ".sprite3"));
    }
}
