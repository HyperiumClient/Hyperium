/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.installer;

import java.io.File;

public enum ReleaseChannel {
    STABLE("latest-stable"),
    DEV("latest-dev"),
    LOCAL(getLocalHyperium());

    private String release;
    ReleaseChannel(String release) {
        this.release = release;
    }

    private static String getLocalHyperium(){
        return new File(new File(".").toPath().getRoot().toFile(), "Hyperium").getAbsolutePath();
    }
}
