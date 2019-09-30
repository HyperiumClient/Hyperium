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

    public static boolean FIRST_START = true;

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
        int j = height / 4 + 48;
        addSingleplayerMultiplayerButtons(j - 10, 24);
        addDefaultStyleOptionsButton(j);
    }

    /**
     * Override buttons
     *
     * @author Cubxity
     */
    public void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        addDefaultStyleSingleplayerMultiplayerButtons(p_73969_1_, p_73969_2_);
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

        fontRendererObj.drawStringWithShadow("Hyperium " + Metadata.getVersion(), 3, resolution.getScaledHeight() - fontRendererObj.FONT_HEIGHT, 0x55FFFFFF);
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
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiOptions(this, mc.gameSettings));
                break;

            case 1:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiSelectWorld(this));
                break;

            case 2:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(this));
                break;

            case 4:
                if (Settings.CONFIRM_QUIT) {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiConfirmQuit());
                } else {
                    mc.shutdown();
                }
                break;

            case 5:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                break;

            case 15:
                HyperiumMainGui.INSTANCE.show();
                break;

            case 16:
                GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
                guiMultiplayer.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
                ((IMixinGuiMultiplayer) guiMultiplayer).makeDirectConnect();
                String hostName = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "stuck.hypixel.net" : "mc.hypixel.net";
                ServerData data = new ServerData("hypixel", hostName, false);
                ((IMixinGuiMultiplayer) guiMultiplayer).setIp(data);
                guiMultiplayer.confirmClicked(true, 0);
                break;

            case 100:
                HyperiumMainGui.INSTANCE.setTab(1);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumMainGui.INSTANCE);
                break;

            case 101:
                mc.displayGuiScreen(new ChangeBackgroundGui(this));
                break;
        }
    }

    public void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        buttonList.add(new GuiButton(1, width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        //Change realms button ID to 16 to avoid conflicts
        buttonList.add(hypixelButton = new GuiButton(16, width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 200, 20, I18n.format("button.ingame.joinhypixel")));

        buttonList.add(new GuiButton(15, width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("button.ingame.hyperiumsettings")));
    }

    public void addDefaultStyleOptionsButton(int j) {
        buttonList.add(new GuiButton(0, width / 2 - 100, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(4, width / 2 + 2, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
        buttonList.add(new GuiButton(100, width / 2 - 100, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.cosmeticshop")));
        buttonList.add(new GuiButton(101, width / 2 + 2, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.changebackground")));
    }
}
