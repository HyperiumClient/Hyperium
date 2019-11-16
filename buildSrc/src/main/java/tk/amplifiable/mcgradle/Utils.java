package tk.amplifiable.mcgradle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import groovy.json.StringEscapeUtils;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static void writeString(String str, File file) throws IOException {
        if (file.getParentFile() != null) {
            MCGradleConstants.prepareDirectory(file.getParentFile());
        }
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.close();
    }

    public static JsonObject readJsonObj(File file) throws IOException {
        FileReader reader = new FileReader(file);
        JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();
        return obj;
    }

    public static File getCacheFile(String name) {
        return new File(MCGradleConstants.CACHE_DIRECTORY, name);
    }

    public static JsonObject readVersionManifest() throws IOException {
        return readJsonObj(getCacheFile(Names.VERSION_MANIFEST));
    }

    public static JsonObject readVersionJson(String version) throws IOException {
        return readJsonObj(getCacheFile(String.format(Names.VERSION_JSON, version)));
    }

    public static String escape(String s) {
        return StringEscapeUtils.escapeJava(s);
    }

    public static String sha1(File file) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new GradleException("SHA-1 isn't known", e);
        }
        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int length = input.read(buffer);
            while (length != -1) {
                digest.update(buffer, 0, length);
                length = input.read(buffer);
            }
            return new HexBinaryAdapter().marshal(digest.digest()).toLowerCase();
        }
    }

    public static class ExtractingFileVisitor implements FileVisitor {
        private final File out;

        public ExtractingFileVisitor(File out) {
            this.out = out;
        }

        @Override
        public void visitDir(FileVisitDetails dirDetails) {
            File dir = new File(out, dirDetails.getPath());
            MCGradleConstants.prepareDirectory(dir);
        }

        @Override
        public void visitFile(FileVisitDetails fileDetails) {
            File out = new File(this.out, fileDetails.getPath());
            if (out.getParentFile() != null) {
                MCGradleConstants.prepareDirectory(out.getParentFile());
            }
            fileDetails.copyTo(out);
        }
    }
}
