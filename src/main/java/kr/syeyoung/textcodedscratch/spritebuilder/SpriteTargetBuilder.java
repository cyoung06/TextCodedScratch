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
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SpriteTargetBuilder {
    private SpriteDefinition definition;
    private ScriptBuilder builder = new ScriptBuilder();
    private JSONObject built = new JSONObject();
    public SpriteTargetBuilder(SpriteDefinition definition) {
        this.definition = definition;
    }

    public void build() {
        built.put("name", definition.getSpriteName().getName().getValue());
        buildProps();
        buildVariables();
        buildScriptParts();
        try {
            buildCostumes();
            buildSounds();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void buildToFile(File f2) throws IOException, NoSuchAlgorithmException {
        build();
        Objects.requireNonNull(f2).getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(f2);
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


    private void buildCostumes() throws IOException, NoSuchAlgorithmException {
        JSONArray costumeList = new JSONArray();
        for (CostumeDeclaration cd:definition.getCostumes().values()) {
            File file = new File(cd.getLocation().getValue(String.class));
            if (!file.exists()) throw new ParsingGrammarException("Costume file not found :: "+file.getAbsolutePath());

            String varName = cd.getName().getMatchedStr();
            String md5 = FileUtils.calcMD5Hash(file);

            JSONObject costume = new JSONObject();
            costume.put("name", varName);
            costume.put("assetId", md5);
            costume.put("md5ext", md5+"."+FileUtils.getExtension(file));
            costume.put("dataFormat", FileUtils.getExtension(file));

            costume.put("bitmapResolution", 2);
            costume.put("rotationCenterX", 0);
            costume.put("rotationCenterY", 0);

            costumeList.put(costume);
        }
        built.put("costumes", costumeList);
    }
    private void buildSounds() throws IOException, NoSuchAlgorithmException {
        JSONArray costumeList = new JSONArray();
        for (SoundDeclaration cd:definition.getSounds().values()) {
            File file = new File(cd.getLocation().getValue(String.class));
            if (!file.exists()) throw new ParsingGrammarException("Sound file not found :: "+file.getAbsolutePath());

            String varName = cd.getName().getMatchedStr();
            String md5 = FileUtils.calcMD5Hash(file);

            JSONObject costume = new JSONObject();
            costume.put("name", varName);
            costume.put("assetId", md5);
            costume.put("md5ext", md5+"."+FileUtils.getExtension(file));
            costume.put("dataFormat", FileUtils.getExtension(file));

            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = audioInputStream.getFormat();
                costume.put("rate", format.getSampleRate());
                costume.put("sampleCount", audioInputStream.getFrameLength() * format.getChannels() * format.getSampleRate());
            } catch (UnsupportedAudioFileException e) {
                throw new ParsingGrammarException("Declared as a sound file, but is not a soundfile :: "+file.getAbsolutePath());
            }


            costumeList.put(costume);
        }
        built.put("sounds", costumeList);
    }


    private void buildVariables() {
        JSONObject variables = new JSONObject();
        for (VariableDeclaration varDec: definition.getVariables().values()) {
            variables.put("$TCS_V$_"+varDec.getName().getMatchedStr(), new JSONArray().put(varDec.getName().getMatchedStr()).put(varDec.getDefaultValue().getValue()));
        }

        JSONObject lists = new JSONObject();
        for (ListDeclaration varDec: definition.getLists().values()) {
            JSONArray arr = new JSONArray().put(varDec.getName().getMatchedStr());
            for (ConstantNode cn:varDec.getDefaultValues())
                arr.put(cn.getValue());
            lists.put("$TCS_L$_"+varDec.getName().getMatchedStr(), arr);
        }

        built.put("variables", variables).put("lists", lists).put("broadcasts", new JSONObject()).put("comments", new JSONObject());
    }

    private void buildProps() {
        built.put("isStage", false);
        built.put("currentCostume", 0);
        built.put("volume", 100);
        built.put("visible", false);
        built.put("x", 0);
        built.put("y", 0);
        built.put("size", 100);
        built.put("direction", 90);
        built.put("draggable", false);
        built.put("rotationStyle", "all around");
    }
    private void buildScriptParts() {
        List<EventDeclaration> events = definition.getEvents();
        Collection<FunctionDeclaration> functions = definition.getFunctions().values();

        for (EventDeclaration eventDeclaration:events) {
            if (eventDeclaration == null) continue;
            eventDeclaration.buildJSON(null, null, builder);
        }

        for (FunctionDeclaration functionDeclaration:functions) {
            if (functionDeclaration == null) continue;
            functionDeclaration.buildJSON(null, null, builder);
        }

        built.put("blocks", builder.getJSON());
    }
}
