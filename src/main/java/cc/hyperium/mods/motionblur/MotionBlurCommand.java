/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.motionblur;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.renderer.HyperiumEntityRenderer;
import org.apache.commons.lang3.math.NumberUtils;

public class MotionBlurCommand implements BaseCommand {

    public void onExecute(String[] args) {
        if (args.length != 1) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Usage: /motionblur <0 - 7>.");
        } else {
            if (MotionBlurMod.isFastRenderEnabled()) {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler()
                    .sendMessage("Motion blur does not work if Fast Render is enabled, please disable it in Options > Video Settings > Performance.");
                return;
            }

            int amount = NumberUtils.toInt(args[0], -1);

            if (amount < 0 || amount > 7) {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Invalid motion blur amount.");
                return;
            }

            if (amount != 0) {
                Settings.MOTION_BLUR_ENABLED = true;
                Settings.MOTION_BLUR_AMOUNT = (float) amount;

                try {
                    MotionBlurMod.applyShader();
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion blur enabled.");
                } catch (Throwable var5) {
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Failed to enable motion blur.");
                    var5.printStackTrace();
                }
            } else {
                Settings.MOTION_BLUR_ENABLED = false;
                HyperiumEntityRenderer.INSTANCE.disableBlurShader();
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion blur disabled.");
            }
        }
    }

    public String getName() {
        return "motionblur";
    }

    public String getUsage() {
        return "/motionblur <0 - 7>";
    }
}
