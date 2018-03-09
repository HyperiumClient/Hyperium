/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.mods.capturex.render;

import cc.hyperium.mods.capturex.CaptureCore;
import cc.hyperium.mods.capturex.CapturePack;

import java.io.File;
import java.io.IOException;

public class FFMpegHelper {
    
    private final File captureXDir = CaptureCore.captureXDir;
    
    public File run(final CapturePack capturePack, final String ffmpegExecutable,
        final String outputName, String prefix) throws IOException, InterruptedException {
        capturePack.renderFrames();
        this.captureXDir.mkdir();
        ProcessBuilder builder = new ProcessBuilder()
            .command(ffmpegExecutable, "-framerate", "20", "-i", "img%03d.png", "-c:v", "libx264", "-pix_fmt", "yuv420p", outputName + ".mp4")
            .directory(new File(this.captureXDir, prefix+"-" + capturePack.getTimestamp()))
            .inheritIO()
            .redirectErrorStream(true);
        Process process = builder.start();
        int i = process.waitFor();
        System.gc();
        if (i != 0) {
            return null;
        }
        return new File(this.captureXDir, outputName + ".mp4");
    }
}
