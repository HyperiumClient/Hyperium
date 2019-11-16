package tk.amplifiable.mcgradle.tasks.mc;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import net.minecraftforge.srg2source.rangeapplier.MethodData;
import net.minecraftforge.srg2source.rangeapplier.SrgContainer;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;
import tk.amplifiable.mcgradle.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TaskGenerateMappings extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @OutputDirectory
    private File outputDirectory = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings");

    @TaskAction
    public void generate() throws IOException {
        MCGradleConstants.prepareDirectory(outputDirectory);
        Utils.ExtractingFileVisitor visitor = new Utils.ExtractingFileVisitor(outputDirectory);
        for (File source : getFiles1()) {
            getProject().zipTree(source).visit(visitor);
        }
        for (File source : getFiles2()) {
            getProject().zipTree(source).visit(visitor);
        }

        File inSrgFile = new File(outputDirectory, "joined.srg");
        File inExc = new File(outputDirectory, "joined.exc");
        File methodsCsv = new File(outputDirectory, "methods.csv");
        File fieldsCsv = new File(outputDirectory, "fields.csv");

        // we want Forge's naming so we're compatible with things like Mixin
        File notchToSrgFile = new File(outputDirectory, "srgs/notch-srg.srg");
        File notchToMcpFile = new File(outputDirectory, "srgs/notch-mcp.srg");
        File srgToMcpFile = new File(outputDirectory, "srgs/srg-mcp.srg");
        File mcpToSrgFile = new File(outputDirectory, "srgs/mcp-srg.srg");
        File mcpToNotchFile = new File(outputDirectory, "srgs/mcp-notch.srg");
        File srgExcFile = new File(outputDirectory, "srgs/srg.exc");
        File mcpExcFile = new File(outputDirectory, "srgs/mcp.exc");
        Map<String, String> methods = Maps.newHashMap();
        Map<String, String> fields = Maps.newHashMap();
        CSVReader reader = new CSVReader(Files.newReader(methodsCsv, Charset.defaultCharset()), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.NULL_CHARACTER, 1, false);
        for (String[] s : reader.readAll()) {
            methods.put(s[0], s[1]);
        }
        reader = new CSVReader(Files.newReader(fieldsCsv, Charset.defaultCharset()), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.NULL_CHARACTER, 1, false);
        for (String[] s : reader.readAll()) {
            fields.put(s[0], s[1]);
        }

        SrgContainer inSrg = new SrgContainer().readSrg(inSrgFile);
        MCGradleConstants.prepareDirectory(notchToSrgFile.getParentFile());
        MCGradleConstants.prepareDirectory(notchToMcpFile.getParentFile());
        MCGradleConstants.prepareDirectory(srgToMcpFile.getParentFile());
        MCGradleConstants.prepareDirectory(mcpToSrgFile.getParentFile());
        MCGradleConstants.prepareDirectory(mcpToNotchFile.getParentFile());

        BufferedWriter notchToSrg = Files.newWriter(notchToSrgFile, StandardCharsets.UTF_8);
        BufferedWriter notchToMcp = Files.newWriter(notchToMcpFile, StandardCharsets.UTF_8);
        BufferedWriter srgToMcp = Files.newWriter(srgToMcpFile, StandardCharsets.UTF_8);
        BufferedWriter mcpToSrg = Files.newWriter(mcpToSrgFile, StandardCharsets.UTF_8);
        BufferedWriter mcpToNotch = Files.newWriter(mcpToNotchFile, StandardCharsets.UTF_8);

        String line, temp, mcpName;
        for (Map.Entry<String, String> entry : inSrg.packageMap.entrySet()) {
            line = "PK: " + entry.getKey() + " " + entry.getValue();
            notchToSrg.write(line);
            notchToSrg.newLine();

            notchToMcp.write(line);
            notchToMcp.newLine();

            mcpToNotch.write("PK: " + entry.getValue() + " " + entry.getKey());
            mcpToNotch.newLine();
        }

        for (Map.Entry<String, String> entry : inSrg.classMap.entrySet()) {
            line = "CL: " + entry.getKey() + " " + entry.getValue();

            notchToSrg.write(line);
            notchToSrg.newLine();

            notchToMcp.write(line);
            notchToMcp.newLine();

            srgToMcp.write("CL: " + entry.getValue() + " " + entry.getValue());
            srgToMcp.newLine();

            mcpToSrg.write("CL: " + entry.getValue() + " " + entry.getValue());
            mcpToSrg.newLine();

            mcpToNotch.write("CL: " + entry.getValue() + " " + entry.getKey());
            mcpToNotch.newLine();
        }

        for (Map.Entry<String, String> entry : inSrg.fieldMap.entrySet()) {
            line = "FD: " + entry.getKey() + " " + entry.getValue();
            notchToSrg.write(line);
            notchToSrg.newLine();

            temp = entry.getValue().substring(entry.getValue().lastIndexOf('/') + 1);
            mcpName = entry.getValue();
            if (fields.containsKey(temp)) {
                mcpName = mcpName.replace(temp, fields.get(temp));
            }

            notchToMcp.write("FD: " + entry.getKey() + " " + mcpName);
            notchToMcp.newLine();

            srgToMcp.write("FD: " + entry.getValue() + " " + mcpName);
            srgToMcp.newLine();

            mcpToSrg.write("FD: " + mcpName + " " + entry.getValue());
            mcpToSrg.newLine();

            mcpToNotch.write("FD: " + mcpName + " " + entry.getKey());
            mcpToNotch.newLine();
        }

        for (Map.Entry<MethodData, MethodData> entry : inSrg.methodMap.entrySet()) {
            line = "MD: " + entry.getKey() + " " + entry.getValue();

            notchToSrg.write(line);
            notchToSrg.newLine();

            temp = entry.getValue().name.substring(entry.getValue().name.lastIndexOf('/') + 1);
            mcpName = entry.getValue().toString();
            if (methods.containsKey(temp)) {
                mcpName = mcpName.replace(temp, methods.get(temp));
            }

            notchToMcp.write("MD: " + entry.getKey() + " " + mcpName);
            notchToMcp.newLine();

            srgToMcp.write("MD: " + entry.getValue() + " " + mcpName);
            srgToMcp.newLine();

            mcpToSrg.write("MD: " + mcpName + " " + entry.getValue());
            mcpToSrg.newLine();

            mcpToNotch.write("MD: " + mcpName + " " + entry.getKey());
            mcpToNotch.newLine();
        }

        notchToSrg.flush();
        notchToSrg.close();

        notchToMcp.flush();
        notchToMcp.close();

        srgToMcp.flush();
        srgToMcp.close();

        mcpToSrg.flush();
        mcpToSrg.close();

        mcpToNotch.flush();
        mcpToNotch.close();

        MCGradleConstants.prepareDirectory(srgExcFile.getParentFile());
        MCGradleConstants.prepareDirectory(mcpExcFile.getParentFile());

        BufferedWriter srgOut = Files.newWriter(srgExcFile, StandardCharsets.UTF_8);
        BufferedWriter mcpOut = Files.newWriter(mcpExcFile, StandardCharsets.UTF_8);

        List<String> excLines = Files.readLines(inExc, StandardCharsets.UTF_8);
        String[] split;
        for (String line1 : excLines) {
            srgOut.write(line1);
            srgOut.newLine();
            split = line1.split("=");
            int sigIndex = split[0].indexOf('(');
            int dotIndex = split[0].indexOf('.');
            if (sigIndex == -1 || dotIndex == -1) {
                mcpOut.write(line1);
                mcpOut.newLine();
                continue;
            }

            String name = split[0].substring(dotIndex + 1, sigIndex);
            if (methods.containsKey(name)) {
                name = methods.get(name);
            }
            mcpOut.write(split[0].substring(0, dotIndex) + "." + name + split[0].substring(sigIndex) + "=" + split[1]);
            mcpOut.newLine();
        }

        srgOut.flush();
        srgOut.close();

        mcpOut.flush();
        mcpOut.close();
    }

    @InputFiles
    public FileCollection getFiles1() {
        return getProject().getConfigurations().getByName(Names.MCP_DATA_CONF);
    }

    @InputFiles
    public FileCollection getFiles2() {
        return getProject().getConfigurations().getByName(Names.MCP_MAPPINGS_CONF);
    }

    @Input
    public String getVersion() {
        return version;
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }
}
