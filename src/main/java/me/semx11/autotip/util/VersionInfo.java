/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.semx11.autotip.util;

import cc.hyperium.utils.ChatColor;

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
