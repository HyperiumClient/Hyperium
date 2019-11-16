package tk.amplifiable.mcgradle.tasks.properties;

import com.google.common.collect.Maps;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TaskGenerateProperties extends DefaultTask {
    @Input
    public Map<String, String> properties = Maps.newHashMap();

    @OutputFile
    public File out = new File(getProject().getRootDir(), "generated/src/main/java/tk/amplifiable/mcgradle/Properties.java");

    @TaskAction
    public void generate() throws IOException {
        MCGradleConstants.prepareDirectory(out.getParentFile());
        StringBuilder builder = new StringBuilder();
        builder.append("package tk.amplifiable.mcgradle;\n" +
                "\n" +
                "public final class Properties {\n");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            builder.append("    public static final String ").append(getJavaName(property.getKey())).append(" = \"").append(Utils.escape(property.getValue())).append("\";\n");
        }
        builder.append("\n    private Properties() {\n    }\n");
        builder.append("}\n");
        FileWriter writer = new FileWriter(out);
        writer.write(builder.toString());
        writer.close();
    }

    private String getJavaName(String original) {
        StringBuilder newName = new StringBuilder();
        for (char c : original.toCharArray()) {
            if (c >= 0x41 && c <= 0x5A) { // is uppercase
                newName.append("_").append(c);
            } else if (c >= 0x61 && c <= 0x7A) { // is lowercase
                newName.append((char) (c - 0x20));
            } else {
                newName.append(c);
            }
        }
        return newName.toString();
    }

    @Input
    public Map<String, String> getProperties() {
        return properties;
    }

    @OutputFile
    public File getOut() {
        return out;
    }
}
