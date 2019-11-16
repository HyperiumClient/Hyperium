package tk.amplifiable.mcgradle;

import groovy.lang.GroovyObjectSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extension extends GroovyObjectSupport {
    public String version = "1.8.9";
    public String mappingChannel = "stable";
    public String mappingVersion = "22";
    public String runDirectory = "run";
    public String clientMainClass = "net.minecraft.client.main.Main";
    public String serverMainClass = "net.minecraft.server.dedicated.DedicatedServer";
    public String kotlinVersion = null;

    public String javaVersion = "1.8";

    public Map<String, String> properties = new HashMap<>();

    public void mappingChannel(String mappingChannel) {
        this.mappingChannel = mappingChannel;
    }

    public void mappingVersion(String mappingVersion) {
        this.mappingVersion = mappingVersion;
    }

    public void version(String version) {
        this.version = version;
    }

    public void kotlinVersion(String version) {
        this.kotlinVersion = version;
    }

    public void runDirectory(String directory) {
        this.runDirectory = directory;
    }

    public void clientMainClass(String mc) {
        this.clientMainClass = mc;
    }

    public void serverMainClass(String mc) {
        this.serverMainClass = mc;
    }

    @Override
    public Object getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public void setProperty(String property, Object newValue) {
        if (!(newValue instanceof String)) {
            throw new IllegalStateException("Invalid property type");
        }
        properties.put(property, newValue.toString());
    }
}
