package me.semx11.autotip.util;

import java.util.Arrays;
import java.util.List;
import me.semx11.autotip.chat.ChatColor;

public class VersionInfo {

    private Version version;
    private Severity severity;
    private List<String> changelog;

    public VersionInfo(Version version, Severity severity, String... changelog) {
        this(version, severity, Arrays.asList(changelog));
    }

    public VersionInfo(Version version, Severity severity, List<String> changelog) {
        this.version = version;
        this.severity = severity;
        this.changelog = changelog;
    }

    public Version getVersion() {
        return version;
    }

    public Severity getSeverity() {
        return severity;
    }

    public List<String> getChangelog() {
        return changelog;
    }

    public enum Severity {
        OPTIONAL, ADVISED, CRITICAL;

        public String toColoredString() {
            ChatColor color;
            switch (this) {
                default:
                case OPTIONAL:
                    color = ChatColor.GREEN;
                    break;
                case ADVISED:
                    color = ChatColor.YELLOW;
                    break;
                case CRITICAL:
                    color = ChatColor.RED;
                    break;
            }
            return color + this.toString();
        }
    }

}
