package kr.syeyoung.textcodedscratch.spritebuilder;

import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.*;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.ConstantNode;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SB3ProjectBuilder {
    private List<SpriteDefinition> definitions;
    private SpriteDefinition stage;
    private long lastScriptCounter = 0;
    private JSONObject built = new JSONObject();
    public SB3ProjectBuilder(List<SpriteDefinition> definitions) {
        this.definitions = definitions;
        if (this.definitions.stream().filter(sf -> sf.isStage()).count() != 1) throw new ParsingGrammarException("No Stage defined or duplicate stages defined");
        stage = this.definitions.stream().filter(sf -> sf.isStage()).findFirst().get();
    }

    public void build() {
        addVariablesToStage();
        buildMeta();
        buildMontiors();
        buildExtensions();
        buildTargets();
    }

    private void buildMeta() {
        built.put("meta", new JSONObject().put("semver", "3.0.0").put("vm", "0.2.0-prerelease.20200621143012").put("agent", "TextCodedScratch (https://github.com/cyoung06/TextCodedScratch) 1.0.0"));
    }

    private void buildExtensions() {
        Set<String> set = definitions.stream().flatMap(sd -> sd.getExtensionDeclarations().stream()).map(ed -> ed.getName().getValue(String.class)).collect(Collectors.toSet());
        JSONArray array = new JSONArray(set);
        built.put("extensions", array);
    }

    private void buildMontiors() {
        built.put("monitors", new JSONArray());
    }

    private Set<Resource> resources = new HashSet<>();
    private void buildTargets() {
        JSONArray targets = new JSONArray();
        for (SpriteDefinition sd:definitions) {
            SpriteTargetBuilder stb = new SpriteTargetBuilder(sd, 0);
            stb.omitGlobalVars = true;
            stb.build();
            targets.put(stb.getJSON());
            resources.addAll(stb.getResources());
            lastScriptCounter += stb.getBuilderIndex() + 1;
        }
        built.put("targets", targets);
    }

    private void addVariablesToStage() {
        for (SpriteDefinition sd:definitions) {
            if (sd.isStage()) continue;;

            Map<String, VariableDeclaration> vars =  sd.getVariables();
            for (VariableDeclaration var:vars.values()) {
                if (var.isGlobal()) {
                    stage.putVariable(var);
                }
            }
            Map<String, ListDeclaration> lists = sd.getLists();
            for (ListDeclaration list:lists.values()) {
                if (list.isGlobal()) stage.getLists().put(list.getName().getMatchedStr(), list);
            }
        }
    }

    public void buildToFile(File f2) throws IOException, NoSuchAlgorithmException {
        build();
        if (Objects.requireNonNull(f2).getParent() != null)
            f2.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(f2);
             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos))) {
            for (Resource r:resources) {
                System.out.println("Including resource "+r.getFile().getName()+ " to binary / hash: "+r.getHash());
                File f = r.getFile();
                String hash = r.getHash();
                zos.putNextEntry(new ZipEntry(hash + "." + FileUtils.getExtension(f)));
                Files.copy(f.toPath(), zos);
                zos.flush();
                zos.closeEntry();
            }
            {
                System.out.println("Including sprite.json to binary");
                zos.putNextEntry(new ZipEntry("project.json"));
                OutputStreamWriter osw = new OutputStreamWriter(zos);
                getJSON().write(osw);
                osw.flush();
                zos.flush();
                zos.closeEntry();
            }
        }
    }

    public JSONObject getJSON() {
        return built;
    }




}
