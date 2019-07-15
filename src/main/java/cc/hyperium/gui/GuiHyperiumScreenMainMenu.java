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
import cc.hyperium.config.Settings;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.handlers.handlers.SettingsMigrator;
import cc.hyperium.mixinsimp.client.gui.IMixinGuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;

public class GuiHyperiumScreenMainMenu extends GuiHyperiumScreen implements GuiYesNoCallback {

    public static boolean FIRST_START = true;
    private final ResourceLocation exit = new ResourceLocation("textures/material/exit.png");
    private final ResourceLocation people_outline = new ResourceLocation("textures/material/people-outline.png");
    private final ResourceLocation person_outline = new ResourceLocation("textures/material/person-outline.png");
    private final ResourceLocation settings = new ResourceLocation("textures/material/settings.png");
    private final ResourceLocation hIcon = new ResourceLocation("textures/h_icon.png");

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
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j - 10, 24);

        switch (getStyle()) {
            case DEFAULT:
                addDefaultStyleOptionsButton(j);
                break;
            case HYPERIUM:
                addHyperiumStyleOptionsButton(j);
                break;
        }
    }

    /**
     * Override buttons
     *
     * @author Cubxity
     */
    public void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        switch (getStyle()) {
            case DEFAULT:
                addDefaultStyleSingleplayerMultiplayerButtons(p_73969_1_, p_73969_2_);
                break;
            case HYPERIUM:
                addHyperiumStyleSingleplayerMultiplayerButtons();
                break;
        }
    }

    /**
     * Override drawScreen method
     *
     * @author Cubxity
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            if (Settings.CONFIRM_QUIT) {
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiConfirmQuit());
            } else {
                this.mc.shutdown();
            }
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(guiyesno);
            }
        }

        switch (getStyle()) {
            case DEFAULT:
                if (button.id == 15)
                    HyperiumMainGui.INSTANCE.show();
                if (button.id == 16) {
                    GuiMultiplayer p_i1182_1_ = new GuiMultiplayer(new GuiMainMenu());
                    p_i1182_1_.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
                    ((IMixinGuiMultiplayer) p_i1182_1_).makeDirectConnect();
                    String hostName = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "stuck.hypixel.net" : "mc.hypixel.net";
                    ServerData data = new ServerData("hypixel", hostName, false);
                    ((IMixinGuiMultiplayer) p_i1182_1_).setIp(data);
                    p_i1182_1_.confirmClicked(true, 0);
                }
                break;
            case HYPERIUM:
                if (button.id == 15)
                    HyperiumMainGui.INSTANCE.show();
                break;
        }

        if (button.id == 17)
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new ChangeBackgroundGui(this));
        if (button.id == 100) {
            HyperiumMainGui.INSTANCE.setTab(1);
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumMainGui.INSTANCE);
        }

    }

    public void addHyperiumStyleSingleplayerMultiplayerButtons() {
        this.buttonList.add(new GuiButton(1, this.width / 2 - getIntendedWidth(295), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(2, this.width / 2 - getIntendedWidth(175), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(15, this.width / 2 + getIntendedWidth(65), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 2 - getIntendedHeight(-60), I18n.format("button.menu.cosmeticshop")));
        this.buttonList.add(new GuiButton(17, this.width / 2 - 100, this.height / 2 - getIntendedHeight(-95), I18n.format("button.menu.changebackground")));
    }

    public void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        //Change realms button ID to 16 to avoid conflicts
        this.buttonList.add(this.hypixelButton = new GuiButton(16, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 200, 20, I18n.format("button.ingame.joinhypixel")));

        this.buttonList.add(new GuiButton(15, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("button.ingame.hyperiumsettings")));
    }

    public void addHyperiumStyleOptionsButton(int j) {
        this.buttonList.add(new GuiButton(0, width / 2 - getIntendedWidth(55), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(4, width / 2 + getIntendedWidth(185), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
    }

    public void addDefaultStyleOptionsButton(int j) {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.cosmeticshop")));
        this.buttonList.add(new GuiButton(17, this.width / 2 + 2, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.changebackground")));
    }

    @Override
    public void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {

        GlStateManager.pushMatrix();

        super.drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
        // Draw icons on buttons
        TextureManager tm = mc.getTextureManager();

        tm.bindTexture(person_outline);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(285), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(people_outline);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(165), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(settings);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(45), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(hIcon);
        drawScaledCustomSizeModalRect(this.width / 2 + getIntendedWidth(85), this.height / 2 - getIntendedHeight(35), 0, 0, 104, 104, getIntendedWidth(70), getIntendedHeight(70), 104, 104);
        tm.bindTexture(exit);
        drawScaledCustomSizeModalRect(this.width / 2 + getIntendedWidth(195), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);

        GlStateManager.popMatrix();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_B) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new ChangeBackgroundGui(this));
        }
    }
}
