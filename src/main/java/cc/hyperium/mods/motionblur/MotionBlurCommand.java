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
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MotionBlurCommand implements BaseCommand {

    private final Minecraft mc = Minecraft.getMinecraft();

    public void onExecute(String[] args) {
        if (args.length == 0) {
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Usage: /motionblur <0 - 7>");
        } else {
            int amount = Integer.parseInt(args[0]);
            if (amount >= 0 && amount <= 7) {
                if (Hyperium.INSTANCE.getModIntegration().getMotionBlur().isFastRenderEnabled()) {
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion Blur is not compatible with OptiFine's Fast Render.");
                } else {
                    if (mc.entityRenderer.getShaderGroup() != null) {
                        mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }

                    if (amount != 0) {
                        Settings.MOTION_BLUR_ENABLED = true;
                        Settings.MOTION_BLUR_AMOUNT = amount;
                        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
                        mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion Blur enabled with amount " + amount + ".");
                    } else {
                        Settings.MOTION_BLUR_ENABLED = false;
                        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion Blur disabled.");
                    }

                    Hyperium.CONFIG.save();
                }
            } else {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Invalid blur amount, 0 - 7.");
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
