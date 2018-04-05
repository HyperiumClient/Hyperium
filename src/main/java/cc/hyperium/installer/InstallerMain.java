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

package cc.hyperium.installer;

import java.io.File;

public class InstallerMain {
    static ReleaseChannel releaseChannel = ReleaseChannel.DEV;
    /**
     * called when jar is executed
     *
     * @param args command line argument
     */
    public static void main(String... args) {
        String mcDir = null;
        if(args.length > 0)
            mcDir = args[0];
        if(args.length > 1)
            releaseChannel = ReleaseChannel.valueOf(args[1]);
        if(mcDir!=null)
            if(!new File(mcDir).exists()){
                System.out.println("Specified directory does not exist");
                System.exit(1);
            }
        System.out.println("Starting installer...");
        new InstallerFrame(mcDir);
    }
}
