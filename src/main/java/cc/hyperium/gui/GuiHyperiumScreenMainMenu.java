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
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
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
        int j = height / 4 + 48;
        addSingleplayerMultiplayerButtons(j - 10, 24);

        switch (getStyle()) {
            case DEFAULT:
                addDefaultStyleOptionsButton(j);
                break;
            case HYPERIUM:
                addHyperiumStyleOptionsButton();
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
        if (button.id == 0) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiOptions(this, mc.gameSettings));
        }

        if (button.id == 5) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
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
                mc.shutdown();
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

        if (button.id == 100) {
            HyperiumMainGui.INSTANCE.setTab(1);
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumMainGui.INSTANCE);
        }

        if (button.id == 101) {
            mc.displayGuiScreen(new ChangeBackgroundGui(this));
        }

    }

    public void addHyperiumStyleSingleplayerMultiplayerButtons() {
        buttonList.add(new GuiButton(1, width / 2 - getIntendedWidth(295), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        buttonList.add(new GuiButton(2, width / 2 - getIntendedWidth(175), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        buttonList.add(new GuiButton(15, width / 2 + getIntendedWidth(65), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        buttonList.add(new GuiButton(100, width / 2 - 100, height / 2 - getIntendedHeight(-60), I18n.format("button.menu.cosmeticshop")));
    }

    public void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        buttonList.add(new GuiButton(1, width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        //Change realms button ID to 16 to avoid conflicts
        buttonList.add(hypixelButton = new GuiButton(16, width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 200, 20, I18n.format("button.ingame.joinhypixel")));

        buttonList.add(new GuiButton(15, width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("button.ingame.hyperiumsettings")));
    }

    public void addHyperiumStyleOptionsButton() {
        buttonList.add(new GuiButton(0, width / 2 - getIntendedWidth(55), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        buttonList.add(new GuiButton(4, width / 2 + getIntendedWidth(185), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
    }

    public void addDefaultStyleOptionsButton(int j) {
        buttonList.add(new GuiButton(0, width / 2 - 100, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(4, width / 2 + 2, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
        buttonList.add(new GuiButton(100, width / 2 - 100, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.cosmeticshop")));
        buttonList.add(new GuiButton(101, width / 2 + 2, j + 78 + 12 + 24 - 5, 98, 20, I18n.format("button.menu.changebackground")));
    }

    @Override
    public void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {

        GlStateManager.pushMatrix();

        super.drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
        // Draw icons on buttons
        TextureManager tm = mc.getTextureManager();

        tm.bindTexture(person_outline);
        drawScaledCustomSizeModalRect(width / 2 - getIntendedWidth(285), height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(people_outline);
        drawScaledCustomSizeModalRect(width / 2 - getIntendedWidth(165), height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(settings);
        drawScaledCustomSizeModalRect(width / 2 - getIntendedWidth(45), height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(hIcon);
        drawScaledCustomSizeModalRect(width / 2 + getIntendedWidth(85), height / 2 - getIntendedHeight(35), 0, 0, 104, 104, getIntendedWidth(70), getIntendedHeight(70), 104, 104);
        tm.bindTexture(exit);
        drawScaledCustomSizeModalRect(width / 2 + getIntendedWidth(195), height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);

        GlStateManager.popMatrix();
    }
}
