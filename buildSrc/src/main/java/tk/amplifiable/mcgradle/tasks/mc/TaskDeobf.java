package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.oceanlabs.mcp.mcinjector.LVTNaming;
import de.oceanlabs.mcp.mcinjector.MCInjectorImpl;
import net.md_5.specialsource.Jar;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.RemapperProcessor;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

public class TaskDeobf extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File fieldCsv = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/fields.csv");

    @InputFile
    private File methodCsv = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/methods.csv");

    @InputFile
    private File jar = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/merged.jar");

    @InputFile
    private File srg = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/srgs/notch-srg.srg");

    @InputFile
    private File exceptorCfg = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/srgs/srg.exc");

    @InputFile
    private File exceptorJson = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/mappings/exceptor.json");

    @OutputFile
    private File out = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/deobfuscated.jar");

    @TaskAction
    public void deobfuscate() throws IOException {
        File tempObfJar = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/deobfed_temp.jar");
        deobfuscateJar(jar, tempObfJar, srg);
        applyExceptor(tempObfJar, out, exceptorCfg);
    }

    private void deobfuscateJar(File jar, File out, File srg) throws IOException {
        JarMapping mapping = new JarMapping();
        mapping.loadMappings(srg);
        RemapperProcessor srgProcessor = new RemapperProcessor(null, mapping, null);
        JarRemapper remapper = new JarRemapper(srgProcessor, mapping, null);
        Jar input = Jar.init(jar);
        JointProvider inheritanceProvider = new JointProvider();
        inheritanceProvider.add(new JarProvider(input));
        mapping.setFallbackInheritanceProvider(inheritanceProvider);
        remapper.remapJar(input, out);
    }

    private void applyExceptor(File in, File out, File config) throws IOException {
        String json;
        Map<String, MCInjectorStruct> struct = loadMCIJson(exceptorJson);
        removeUnknown(jar, struct);
        File jsonTmp = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/transformed.json");
        json = jsonTmp.getCanonicalPath();
        Files.write(Paths.get(json), new Gson().toJson(struct).getBytes(StandardCharsets.UTF_8));
        MCInjectorImpl.process(in.getCanonicalPath(), out.getCanonicalPath(), config.getCanonicalPath(), null, null, 0, json, false, true, LVTNaming.LVT);
    }

    private void removeUnknown(File in, Map<String, MCInjectorStruct> config) throws IOException {
        ZipFile zip = new ZipFile(in);
        Iterator<Map.Entry<String, MCInjectorStruct>> entries = config.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, MCInjectorStruct> entry = entries.next();
            String clsName = entry.getKey();
            if (zip.getEntry(clsName + ".class") == null) {
                entries.remove();
                continue;
            }
            MCInjectorStruct struct = entry.getValue();
            if (struct.innerClasses != null) {
                struct.innerClasses.removeIf(innerClass -> zip.getEntry(innerClass.inner_class + ".class") == null);
            }
        }
        zip.close();
    }

    private static Map<String, MCInjectorStruct> loadMCIJson(File json) throws IOException
    {
        FileReader reader = new FileReader(json);
        Map<String, MCInjectorStruct> ret = new LinkedHashMap<>();

        JsonObject object = (JsonObject) JsonParser.parseReader(reader);
        reader.close();

        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            ret.put(entry.getKey(), new Gson().fromJson(entry.getValue(), MCInjectorStruct.class));
        }
        return ret;
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getFieldCsv() {
        return fieldCsv;
    }

    @InputFile
    public File getMethodCsv() {
        return methodCsv;
    }

    @InputFile
    public File getJar() {
        return jar;
    }

    @InputFile
    public File getSrg() {
        return srg;
    }

    @InputFile
    public File getExceptorCfg() {
        return exceptorCfg;
    }

    @InputFile
    public File getExceptorJson() {
        return exceptorJson;
    }

    @OutputFile
    public File getOut() {
        return out;
    }

    public static class MCInjectorStruct
    {
        public EnclosingMethod enclosingMethod = null;
        public List<InnerClass> innerClasses = null;

        public static class EnclosingMethod
        {
            public final String desc;
            public final String name;
            public final String owner;

            EnclosingMethod(String owner, String name, String desc)
            {
                this.owner = owner;
                this.name = name;
                this.desc = desc;
            }
        }

        public static class InnerClass
        {
            public String access;
            public final String inner_class;
            public final String inner_name;
            public final String outer_class;
            public final String start;

            InnerClass(String inner_class, String outer_class, String inner_name, String access, String start)
            {
                this.inner_class = inner_class;
                this.outer_class = outer_class;
                this.inner_name = inner_name;
                this.access = access;
                this.start = start;
            }

            public int getAccess() { return Integer.parseInt(access == null ? "0" : access, 16); }
            public int getStart()  { return Integer.parseInt(start  == null ? "0" : start,  10); }
        }
    }
}
