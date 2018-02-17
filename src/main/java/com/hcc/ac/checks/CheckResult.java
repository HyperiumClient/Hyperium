/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.ac.checks;

public class CheckResult {
    private Level level;
    private String detection;
    private String description;

    public CheckResult(Level level, String detection, String description) {
        this.level = level;
        this.detection = detection;
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public String getDetection() {
        return detection;
    }

    public String getDescription() {
        return description;
    }

    public enum Level{
        CLEAN,
        SLIGHTLY,
        POTENTIALLY,
        DEFINITELY
    }
}
