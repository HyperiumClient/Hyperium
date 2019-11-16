package tk.amplifiable.mcgradle.mc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.gradle.api.artifacts.Dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DependencyUtilities {
    private DependencyUtilities() {
    }

    public static boolean shouldInclude(JsonObject obj) {
        List<Rule> rules = new ArrayList<>();
        if (obj.has("rules")) {
            for (JsonElement element : obj.getAsJsonArray("rules")) {
                rules.add(Rule.parse(element.getAsJsonObject()));
            }
        }
        boolean allowed = rules.isEmpty();
        boolean disallowEncountered = false;
        for (Rule rule : rules) {
            allowed = rule.shouldAllow();
            if (rule.getAction() == RuleAction.DISALLOW && rule.doesNotMatch()) {
                if (!disallowEncountered && !allowed) {
                    allowed = true;
                    continue;
                }
            }
            if (rule.getAction() == RuleAction.DISALLOW) {
                disallowEncountered = true;
            }
        }
        return allowed;
    }

    public static boolean equals(JsonObject obj, Dependency dependency) {
        String name = obj.get("name").getAsString();
        String[] s = name.split(":");
        return Objects.requireNonNull(dependency.getGroup()).equalsIgnoreCase(s[0]) && dependency.getName().equalsIgnoreCase(s[1]) && Objects.requireNonNull(dependency.getVersion()).equalsIgnoreCase(s[2]);
    }

    public static String getDependencyString(JsonObject obj) {
        boolean isNative = obj.has("natives");
        String it = obj.get("name").getAsString();
        if (isNative) {
            JsonObject nativesObj = obj.getAsJsonObject("natives");
            it += ":" + nativesObj.get(Objects.requireNonNull(OS.current()).name().toLowerCase()).getAsString().replace("${arch}", System.getProperty("os.arch").equalsIgnoreCase("x86") ? "32" : "64");
        }
        return it;
    }

    private enum OS {
        WINDOWS,
        LINUX,
        OSX;

        public static OS current() {
            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                return WINDOWS;
            } else if (Os.isFamily(Os.FAMILY_MAC)) {
                return OSX;
            } else if (Os.isFamily(Os.FAMILY_UNIX)) {
                return LINUX; // Linux Is Not Unix, but it's the only thing Ant offers and it works
            } else {
                return null;
            }
        }

        public static OS parseMc(String mc) {
            switch (mc) {
                case "windows":
                    return WINDOWS;
                case "osx":
                    return OSX;
                case "linux":
                    return LINUX;
                default:
                    System.err.println("Unknown OS " + mc);
                    return null;
            }
        }
    }

    private enum RuleAction {
        ALLOW,
        DISALLOW
    }

    private static class Rule {
        private RuleAction action;
        private OS os;
        private String arch;
        private Pattern version;

        Rule(RuleAction action, OS os, String arch, Pattern version) {
            this.action = action;
            this.os = os;
            this.arch = arch;
            this.version = version;
        }

        static Rule parse(JsonObject rule) {
            RuleAction action = null;
            OS os = null;
            String architecture = null;
            String version = null;
            if (rule.has("action")) {
                action = rule.get("action").getAsString().equals("allow") ? RuleAction.ALLOW : RuleAction.DISALLOW;
            }
            if (rule.has("os")) {
                JsonObject osObj = rule.getAsJsonObject("os");
                if (osObj.has("name")) {
                    os = OS.parseMc(osObj.get("name").getAsString());
                }
                if (osObj.has("version")) {
                    version = osObj.get("version").getAsString();
                }
                if (osObj.has("arch")) {
                    architecture = osObj.get("arch").getAsString();
                }
            }
            return new Rule(action, os, architecture, version == null ? null : Pattern.compile(version));
        }

        boolean doesNotMatch() {
            if (os != null) {
                OS current = OS.current();
                if (os != current) {
                    return true;
                }
            }
            if (arch != null) {
                if (!arch.equals(System.getProperty("os.arch"))) {
                    return true;
                }
            }
            if (version != null) {
                return !version.matcher(System.getProperty("os.version")).find();
            }
            return false;
        }

        boolean shouldAllow() {
            if (doesNotMatch()) return false;
            return action == RuleAction.ALLOW;
        }

        RuleAction getAction() {
            return action;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "action=" + action +
                    ", os=" + os +
                    ", arch='" + arch + '\'' +
                    ", version=" + version +
                    '}';
        }
    }
}