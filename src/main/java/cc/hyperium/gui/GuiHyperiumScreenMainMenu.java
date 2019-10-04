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

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.handlers.handlers.SettingsMigrator;
import cc.hyperium.mixinsimp.client.gui.IMixinGuiMultiplayer;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiHyperiumScreenMainMenu extends GuiHyperiumScreen implements GuiYesNoCallback {

    private static boolean FIRST_START = true;

    public GuiHyperiumScreenMainMenu() {
        if (Minecraft.getMinecraft().isFullScreen() && Settings.WINDOWED_FULLSCREEN && FIRST_START) {
            GuiHyperiumScreenMainMenu.FIRST_START = false;
            Minecraft.getMinecraft().toggleFullscreen();
            Minecraft.getMinecraft().toggleFullscreen();
        }

        if (Hyperium.INSTANCE.isFirstLaunch()) {
            new SettingsMigrator().migrate();
        }
    }

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    public void initGui() {
        int center = width / 2;

        buttonList.add(new GuiButton(0, center - 100, getRowPos(2), I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(1, center - 100, getRowPos(3), I18n.format("menu.multiplayer")));
        buttonList.add(hypixelButton = new GuiButton(2, center - 100, getRowPos(4),  I18n.format("button.ingame.joinhypixel")));
        buttonList.add(new GuiButton(3, center - 100, getRowPos(5), I18n.format("button.ingame.hyperiumsettings")));
        buttonList.add(new GuiButton(4, center - 100, getRowPos(6), 98, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(5, center + 2, getRowPos(6), 98, 20, I18n.format("menu.quit")));
        buttonList.add(new GuiButton(6, center - 100, getRowPos(7), 98, 20, I18n.format("button.menu.cosmeticshop")));
        buttonList.add(new GuiButton(7, center + 2, getRowPos(7), 98, 20, I18n.format("button.menu.changebackground")));
    }

    /**
     * Override drawScreen method
     *
     * @author Cubxity
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        renderBackgroundImage();
        ScaledResolution resolution = new ScaledResolution(mc);

        fontRendererObj.drawStringWithShadow("Hyperium " + Metadata.getVersion(), 3, resolution.getScaledHeight() - fontRendererObj.FONT_HEIGHT, -1);
        String s1 = I18n.format("menu.right");
        drawString(fontRendererObj, s1, width - fontRendererObj.getStringWidth(s1) - 2, height - 30, -1);
        s1 = "Made by Sk1er, Kevin, Cubxity, CoalOres,";
        drawString(fontRendererObj, s1, width - fontRendererObj.getStringWidth(s1) - 2, height - 20, -1);
        s1 = "boomboompower, FalseHonesty, and asbyth.";
        drawString(fontRendererObj, s1, width - fontRendererObj.getStringWidth(s1) - 2, height - 10, -1);

        if (PurchaseApi.getInstance() != null && PurchaseApi.getInstance().getSelf() != null) {
            JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
            int credits = response.optInt("remaining_credits");

            fontRendererObj.drawStringWithShadow(mc.getSession().getUsername(), 3, 3, 0xFFFFFF);
            fontRendererObj.drawStringWithShadow(I18n.format("menu.profile.credits", credits), 3, 13, 0xFFFF00);
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(4F, 4F, 1F);
        drawCenteredString(fontRendererObj, Metadata.getModid(), width / 8, 40 / 4, -1);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GuiButton hypixelButton = this.hypixelButton;

        if (hypixelButton != null) {
            hypixelButton.displayString = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? I18n.format("button.ingame.fixhypixelsession") : I18n.format("button.ingame.joinhypixel");
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiSelectWorld(this));
                break;

            case 1:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(this));
                break;

            case 2:
                GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
                guiMultiplayer.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
                ((IMixinGuiMultiplayer) guiMultiplayer).makeDirectConnect();
                String hostName = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "stuck.hypixel.net" : "mc.hypixel.net";
                ServerData data = new ServerData("hypixel", hostName, false);
                ((IMixinGuiMultiplayer) guiMultiplayer).setIp(data);
                guiMultiplayer.confirmClicked(true, 0);
                break;

            case 3:
                HyperiumMainGui.INSTANCE.show();
                break;

            case 4:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiOptions(this, mc.gameSettings));
                break;

            case 5:
                if (Settings.CONFIRM_QUIT) {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiConfirmQuit());
                } else {
                    mc.shutdown();
                }
                break;

            case 6:
                HyperiumMainGui.INSTANCE.setTab(1);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumMainGui.INSTANCE);
                break;

            case 7:
                mc.displayGuiScreen(new ChangeBackgroundGui(this));
                break;
        }
    }

    private int getRowPos(int rowNumber) {
        return 55 + rowNumber * 23;
    }
}
