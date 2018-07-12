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

package cc.hyperium.mixins;

import cc.hyperium.mixinsimp.HyperiumScreenshotHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.nio.IntBuffer;

@Mixin(ScreenShotHelper.class)
class MixinScreenShotHelper {

    @Shadow
    private static IntBuffer pixelBuffer;

    @Shadow
    private static int[] pixelValues;

    private HyperiumScreenshotHelper hyperiumScreenshotHelper = new HyperiumScreenshotHelper((ScreenShotHelper)(Object)this);
    /**
     * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
     * Fixes MC-113208 and MC-117793
     * TODO: Imgur uploader
     *
     * @param gameDirectory
     * @param screenshotName
     * @param width
     * @param height
     * @param buffer
     * @return
     * @author Kevin Brewster, Orange Marhsall, Mojang
     */
    @Overwrite
    public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
       return HyperiumScreenshotHelper.saveScreenshot(gameDirectory, screenshotName, width, height, buffer, pixelBuffer, pixelValues);
    }

}
