package kr.syeyoung.textcodedscratch.spritebuilder;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NativeFunctionBuilderHelper {
    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "sprite3");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(chooser.getSelectedFile()));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (!entry.getName().equals("sprite.json")) {
                zipInputStream.closeEntry();
                continue;
            }

            JSONTokener tokener = new JSONTokener(zipInputStream);
            JSONObject json = new JSONObject(tokener);
            json = json.getJSONObject("blocks");

            for (String key:json.keySet()) {
                JSONObject block = json.getJSONObject(key);
                if (block.get("parent") != JSONObject.NULL) {
                    System.out.println("?? key="+key+" / "+block.toString());
                    continue;
                }

                block.remove("parent");
                block.remove("next");
                block.remove("x");
                block.remove("y");
                block.put("topLevel", false);

                System.out.println("\""+block.toString().replace("\"", "\\\"")+"\"");
            }
            zipInputStream.closeEntry();
        }
    }
}