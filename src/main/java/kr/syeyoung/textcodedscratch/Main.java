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
        FileInputStream fis = new FileInputStream("test.txt");
        Tokenizer tokenizer = new Tokenizer(fis);
        tokenizer.Tokenize();
        for (ParserNode node : tokenizer.getTerminalNodes()) {
            System.out.println(node.getClass().getName() + " - " + node);
        }
        System.out.println("--");
        Parser parser = new Parser(new LinkedList<>(tokenizer.getTerminalNodes().stream().map(t -> (ParserNode)t).collect(Collectors.toList())));
        parser.parse();
        for (ParserNode node : parser.getOutput()) {
            System.out.println(node.getClass().getName() + " - " + node);
        }
        SpriteDefinition definition = parser.getSyntexCheckerRule().getDefinition();
        SpriteTargetBuilder stb = new SpriteTargetBuilder(definition);
        stb.build();

        try (FileOutputStream fos = new FileOutputStream("sprite.sprite3");
                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos))) {

            for (CostumeDeclaration costumeDeclaration : definition.getCostumes().values()) {
                File f = new File(costumeDeclaration.getLocation().getValue(String.class));
                String hash = FileUtils.calcMD5Hash(f);
                zos.putNextEntry(new ZipEntry(hash + "." + FileUtils.getExtension(f)));
                Files.copy(f.toPath(), zos);
                zos.flush();
                zos.closeEntry();
            }
            for (SoundDeclaration costumeDeclaration : definition.getSounds().values()) {
                File f = new File(costumeDeclaration.getLocation().getValue(String.class));
                String hash = FileUtils.calcMD5Hash(f);
                zos.putNextEntry(new ZipEntry(hash + "." + FileUtils.getExtension(f)));
                Files.copy(f.toPath(), zos);
                zos.flush();
                zos.closeEntry();
            }

            {
                zos.putNextEntry(new ZipEntry("sprite.json"));
                OutputStreamWriter osw = new OutputStreamWriter(zos);
                stb.getJSON().write(osw);
                osw.flush();
                zos.flush();
                zos.closeEntry();
            }
        }

    }
}
