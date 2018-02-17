package me.semx11.autotip.util;

import java.util.Arrays;
import java.util.List;

public class VersionInfo {

    private Version version;
    private Severity severity;
    private boolean isBetaVersion;
    private List<String> changelog;

    public VersionInfo(Version version, Severity severity, boolean isBetaVersion,
            String... changelog) {
        this(version, severity, isBetaVersion, Arrays.asList(changelog));
    }

    public VersionInfo(Version version, Severity severity, boolean isBetaVersion,
            List<String> changelog) {
        this.version = version;
        this.severity = severity;
        this.isBetaVersion = isBetaVersion;
        this.changelog = changelog;
    }

    public Version getVersion() {
        return version;
    }

    public Severity getSeverity() {
        return severity;
    }

    public boolean isBetaVersion() {
        return isBetaVersion;
    }

    public List<String> getChangelog() {
        return changelog;
    }

    public enum Severity {
        OPTIONAL, ADVISED, CRITICAL;

        public String toColoredString() {
            String color = "";
            switch (this) {
                case CRITICAL:
                    color = ChatColor.DARK_RED.toString() + ChatColor.BOLD;
                    break;
                case ADVISED:
                    color = ChatColor.YELLOW.toString();
                    break;
                case OPTIONAL:
                    color = ChatColor.GREEN.toString();
                    break;
            }
            return color + this.toString();
        }
    }

}
