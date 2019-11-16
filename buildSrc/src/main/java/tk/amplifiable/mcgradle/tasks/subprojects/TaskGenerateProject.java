package tk.amplifiable.mcgradle.tasks.subprojects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.tooling.GradleConnectionException;
import tk.amplifiable.mcgradle.MCGradle;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;
import tk.amplifiable.mcgradle.Utils;
import tk.amplifiable.mcgradle.mc.DependencyUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TaskGenerateProject extends DefaultTask {
    @Input
    public String javaVersion = MCGradleConstants.EXTENSION.javaVersion;

    @Input
    public List<String> extraRepositories = Lists.newArrayList();

    @Input
    public boolean includeMcDep = getProject().getChildProjects().containsKey("mcdep");

    @Input
    public String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    public File versionJson = Utils.getCacheFile(String.format(Names.VERSION_JSON, version));

    @Input
    public Map<String, String> options = Maps.newHashMap();

    @OutputFile
    public File output = new File(MCGradleConstants.ROOT_PROJECT_DIR, "generated/build.gradle");

    @OutputFile
    public File startOutput = new File(MCGradleConstants.ROOT_PROJECT_DIR, "generated/src/main/java/tk/amplifiable/mcgradle/Start.java");

    public TaskGenerateProject() {
        File runDir = new File(getProject().getRootDir(), MCGradleConstants.EXTENSION.runDirectory);
        addOption("runDirectory", runDir.getAbsolutePath());
        addOption("nativeDirectory", new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/natives").getAbsolutePath());
        addOption("clientMainClass", MCGradleConstants.EXTENSION.clientMainClass);
        addOption("serverMainClass", MCGradleConstants.EXTENSION.serverMainClass);
        addOption("assetsDirectory", new File(MCGradleConstants.CACHE_DIRECTORY, "assets").getAbsolutePath());
    }
    
    public void addOption(String option, Object value) {
        options.put(option, value.toString());
    }

    @TaskAction
    public void generate() throws IOException {
        MCGradleConstants.prepareDirectory(output.getParentFile());
        StringBuilder builder = new StringBuilder();

        JsonObject json = Utils.readJsonObj(versionJson);

        if (MCGradleConstants.EXTENSION.kotlinVersion != null) {
            builder.append("buildscript {\n" + "    repositories {\n" + "        mavenCentral()\n" + "    }\n" + "\n" + "    dependencies {\n" + "        classpath \"org.jetbrains.kotlin:kotlin-gradle-plugin:").append(MCGradleConstants.EXTENSION.kotlinVersion).append("\"\n").append("    }\n").append("}\n\n");
        }

        builder.append(
                "plugins {\n" +
                        "    id 'java'\n");

        builder.append(
                "}\n" +
                "\n");

        if (MCGradleConstants.EXTENSION.kotlinVersion != null) {
            builder.append("apply plugin: 'kotlin'\n\n");
        }

        builder.append("sourceCompatibility = targetCompatibility = '").append(javaVersion).append("'\n" +
                "compileJava.options.encoding = 'UTF-8'\n\ngroup '").append(getProject().getGroup()).append("'\nversion '").append(getProject().getVersion()).append("'\n\n");
        builder.append("repositories {\n" +
                "    mavenCentral()\n" +
                "    maven {\n" +
                "        url 'https://libraries.minecraft.net'\n" +
                "    }\n");
        for (String s : extraRepositories) {
            builder.append("    maven {\n" +
                    "        url '").append(s).append("'\n" +
                    "    }\n");
        }
        builder.append("}\n" +
                "\n" +
                "dependencies {\n");
        if (includeMcDep) {
            builder.append("    implementation project(':mcdep')\n");
        }

        if (MCGradleConstants.EXTENSION.kotlinVersion != null) {
            builder.append("    implementation \"org.jetbrains.kotlin:kotlin-stdlib-jdk8\"\n");
        }

        for (JsonElement element : json.getAsJsonArray("libraries")) {
            JsonObject obj = element.getAsJsonObject();
            if (DependencyUtilities.shouldInclude(obj)) {
                appendDependency(DependencyUtilities.getDependencyString(obj), builder);
            }
            /*if (!isNative) {
                appendDependency(obj.get("name").getAsString(), builder);
            } else {
                appendDependency(obj.get("name").getAsString() + ":" + obj.getAsJsonObject("natives"));
            }*/
        }

        builder.append("}\n");

        FileWriter writer = new FileWriter(output);
        writer.write(builder.toString());
        writer.close();

        MCGradleConstants.prepareDirectory(startOutput.getParentFile());
        String source = IOUtils.toString(getClass().getResourceAsStream("/sources/Start.java"));
        for (Map.Entry<String, String> entry : options.entrySet()) {
            source = source.replace("${" + entry.getKey() + "}", Utils.escape(entry.getValue()));
        }
        writer = new FileWriter(startOutput);
        writer.write(source);
        writer.close();
    }

    private void appendDependency(String dep, StringBuilder builder) {
        builder.append("    implementation '").append(dep).append("'\n");
    }

    @Input
    public Map<String, String> getOptions() {
        if (!options.containsKey("assetIndex")) {
            JsonObject versionJson;
            try {
                versionJson = Utils.readVersionJson(version);
            } catch (IOException e) {
                throw new GradleException("Failed to read version JSON", e);
            }
            options.put("assetIndex", versionJson.getAsJsonObject("assetIndex").get("id").getAsString());
        }
        return options;
    }

    @Input
    public String getJavaVersion() {
        return javaVersion;
    }

    public TaskGenerateProject setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
        return this;
    }

    @Input
    public List<String> getExtraRepositories() {
        return extraRepositories;
    }

    public TaskGenerateProject setExtraRepositories(List<String> extraRepositories) {
        this.extraRepositories = extraRepositories;
        return this;
    }

    @Input
    public boolean isIncludeMcDep() {
        return includeMcDep;
    }

    public TaskGenerateProject setIncludeMcDep(boolean includeMcDep) {
        this.includeMcDep = includeMcDep;
        return this;
    }

    @OutputFile
    public File getOutput() {
        return output;
    }

    @OutputFile
    public File getStartOutput() {
        return startOutput;
    }

    public TaskGenerateProject setOutput(File output) {
        this.output = output;
        return this;
    }

    @Input
    public String getVersion() {
        return version;
    }

    public TaskGenerateProject setVersion(String version) {
        this.version = version;
        return this;
    }

    @InputFile
    public File getVersionJson() {
        return versionJson;
    }

    public TaskGenerateProject setVersionJson(File versionJson) {
        this.versionJson = versionJson;
        return this;
    }
}
