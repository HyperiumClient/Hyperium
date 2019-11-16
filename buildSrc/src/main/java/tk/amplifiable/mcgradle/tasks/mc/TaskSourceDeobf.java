package tk.amplifiable.mcgradle.tasks.mc;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.*;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskSourceDeobf extends DefaultTask {
    private static final Pattern SRG_FINDER = Pattern.compile("func_[0-9]+_[a-zA-Z_]+|field_[0-9]+_[a-zA-Z_]+|p_[\\w]+_\\d+_\\b");
    private static final Pattern METHOD = Pattern.compile("^((?: {3})+|\\t+)(?:[\\w$.\\[\\]]+ )+(func_[0-9]+_[a-zA-Z_]+)\\(");
    private static final Pattern FIELD = Pattern.compile("^((?: {3})+|\\t+)(?:[\\w$.\\[\\]]+ )+(field_[0-9]+_[a-zA-Z_]+) *[=;]");

    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File methodsCsv = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/methods.csv");

    @InputFile
    private File fieldsCsv = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/fields.csv");

    @InputFile
    private File paramsCsv = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/params.csv");

    @InputDirectory
    private File sources = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/patched/mcp");

    @OutputDirectory
    private File output = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/sourceMapped");

    private final Map<String, String> methods = Maps.newHashMap();
    private final Map<String, String> methodDocs = Maps.newHashMap();
    private final Map<String, String> fields = Maps.newHashMap();
    private final Map<String, String> fieldDocs = Maps.newHashMap();
    private final Map<String, String> params = Maps.newHashMap();

    @TaskAction
    private void deobf() throws IOException {
        // read CSV files
        readCsv(methodsCsv, methods, methodDocs, true);

        readCsv(fieldsCsv, fields, fieldDocs, true);

        readCsv(paramsCsv, params, null, false);

        getProject().fileTree(sources).visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {

            }

            @Override
            public void visitFile(FileVisitDetails fileDetails) {
                File outputFile = new File(output, fileDetails.getPath());
                File parent = outputFile.getParentFile();
                if (parent != null) MCGradleConstants.prepareDirectory(parent);
                fileDetails.copyTo(outputFile);
                if (fileDetails.getName().endsWith(".java")) {
                    List<String> lines = Lists.newArrayList();
                    // process
                    try {
                        for (String line : Files.readLines(outputFile, StandardCharsets.UTF_8)) {
                            // Javadoc
                            Matcher matcher = METHOD.matcher(line);
                            if (matcher.find()) {
                                String javadoc = methodDocs.get(matcher.group(2));
                                if (!Strings.isNullOrEmpty(javadoc)) {
                                    insetAboveAnnotations(lines, buildJavadoc(matcher.group(1), javadoc, true));
                                }
                            } else {
                                matcher = FIELD.matcher(line);
                                if (matcher.find()) {
                                    String javadoc = fieldDocs.get(matcher.group(2));
                                    if (!Strings.isNullOrEmpty(javadoc)) {
                                        insetAboveAnnotations(lines, buildJavadoc(matcher.group(1), javadoc, false));
                                    }
                                }
                            }

                            // Change names
                            StringBuffer buf = new StringBuffer();
                            matcher = SRG_FINDER.matcher(line);
                            while (matcher.find()) {
                                String find = matcher.group();
                                if (find.startsWith("p_")) {
                                    find = params.get(find);
                                } else if (find.startsWith("func_")) {
                                    find = methods.get(find);
                                } else if (find.startsWith("field_")) {
                                    find = fields.get(find);
                                }

                                if (find == null) find = matcher.group();
                                matcher.appendReplacement(buf, find);
                            }
                            matcher.appendTail(buf);
                            lines.add(buf.toString());
                        }
                    } catch (IOException e) {
                        throw new GradleException("Failed to remap " + fileDetails.getName(), e);
                    }
                    // write
                    try (FileWriter writer = new FileWriter(outputFile)) {
                        writer.write(Joiner.on('\n').join(lines));
                    } catch (IOException e) {
                        throw new GradleException("Failed to remap " + fileDetails.getName(), e);
                    }
                }
            }
        });
    }

    private static void insetAboveAnnotations(List<String> list, String line) {
        int back = 0;
        while (list.get(list.size() - 1 - back).trim().startsWith("@")) {
            back++;
        }
        list.add(list.size() - back, line);
    }

    private void readCsv(File csv, Map<String, String> data, Map<String, String> docs, boolean addDocs) throws IOException {
        CSVReader reader = new CSVReader(Files.newReader(csv, Charset.defaultCharset()), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.NULL_CHARACTER, 1, false);
        for (String[] s : reader.readAll()) {
            data.put(s[0], s[1]);
            if (addDocs && !s[3].isEmpty()) {
                docs.put(s[0], s[3]);
            }
        }
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getMethodsCsv() {
        return methodsCsv;
    }

    @InputFile
    public File getFieldsCsv() {
        return fieldsCsv;
    }

    @InputFile
    public File getParamsCsv() {
        return paramsCsv;
    }

    @OutputDirectory
    public File getOutput() {
        return output;
    }

    public static String buildJavadoc(String indent, String javadoc, boolean isMethod) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new LinkedList<>();
        for (String line : Splitter.on("\\n").splitToList(javadoc)) {
            list.addAll(wrapText(line, 120 - (indent.length() + 3)));
        }

        if (list.size() > 1 || isMethod) {
            builder.append(indent);
            builder.append("/**");
            builder.append('\n');

            for (String line : list) {
                builder.append(indent);
                builder.append(" * ");
                builder.append(line);
                builder.append('\n');
            }

            builder.append(indent);
            builder.append(" */");
        } else {
            builder.append(indent);
            builder.append("/** ");
            builder.append(javadoc);
            builder.append(" */");
        }

        return builder.toString();
    }

    private static List<String> wrapText(String text, int len) {
        if (text == null) {
            return new ArrayList<>();
        }
        if (len <= 0) {
            return new ArrayList<>(Collections.singletonList(text));
        }
        if (text.length() <= len) {
            return new ArrayList<>(Collections.singletonList(text));
        }
        List<String> lines = new LinkedList<>();
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        int tempNum;
        for (char c : text.toCharArray()) {
            if (c == ' ' || c == ',' || c == '-') {
                word.append(c);
                tempNum = Character.isWhitespace(c) ? 1 : 0;
                if ((line.length() + word.length() - tempNum) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }
                line.append(word);
                word.delete(0, word.length());
            } else {
                word.append(c);
            }
        }
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        List<String> temp = new ArrayList<>(lines.size());
        for (String s : lines) {
            temp.add(s.trim());
        }
        return temp;
    }
}
