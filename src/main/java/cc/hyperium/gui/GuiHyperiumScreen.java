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

package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class GuiHyperiumScreen extends GuiScreen {
    private static ResourceLocation dynamicBackgroundTexture;
    private static File customImage = new File(Minecraft.getMinecraft().mcDataDir, "customImage.png");
    GuiButton serverButton;

    public static void renderBackgroundImage() {
        if (Settings.BACKGROUND.equalsIgnoreCase("CUSTOM")) {
            boolean success = getCustomBackground();

            if (success) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(dynamicBackgroundTexture);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0,
                    ResolutionUtil.current().getScaledWidth(),
                    ResolutionUtil.current().getScaledHeight(),
                    ResolutionUtil.current().getScaledWidth(),
                    ResolutionUtil.current().getScaledHeight());

                return;
            } else {
                Settings.BACKGROUND = "1";
            }
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/material/backgrounds/" + Settings.BACKGROUND + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0,
            ResolutionUtil.current().getScaledWidth(),
            ResolutionUtil.current().getScaledHeight(),
            ResolutionUtil.current().getScaledWidth(),
            ResolutionUtil.current().getScaledHeight());
    }

    private static boolean getCustomBackground() {
        if (dynamicBackgroundTexture != null) return true;

        if (customImage.exists()) {
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(new FileInputStream(customImage));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            if (bufferedImage != null && dynamicBackgroundTexture == null) {
                dynamicBackgroundTexture = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation(customImage.getName(), new DynamicTexture(bufferedImage));
            }
        } else {
            return false;
        }

        return true;
    }
}
