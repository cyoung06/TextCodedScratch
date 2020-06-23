package kr.syeyoung.textcodedscratch.spritebuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

    public static String getExtension(File file ){
        String name = file.getName();
        int lastdot = name.lastIndexOf('.') + 1;
        return name.substring(lastdot);
    }

    public static String calcMD5Hash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(file.toPath());
             DigestInputStream dis = new DigestInputStream(is, md))
        {
            while (dis.read() != -1);
        }

        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            if ((0xff & digest[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & digest[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
        }
        return hexString.toString();
    }
}
