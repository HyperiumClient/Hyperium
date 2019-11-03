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
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.mods.AddonCheckerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GuiHyperiumScreenMainMenu extends GuiHyperiumScreen implements GuiYesNoCallback {

    private static boolean FIRST_START = true;
    private final String createdByTeam = "Created by the Hyperium Team.";
    private int widthCredits;
    private int widthCreditsRest;

    private List<String> hyperiumTips = new ArrayList<>(Arrays.asList(
        "menu.hyperiumtip1",
        "menu.hyperiumtip2",
        "menu.hyperiumtip3",
        "menu.hyperiumtip4",
        "menu.hyperiumtip5",
        "menu.hyperiumtip6",
        "menu.hyperiumtip7",
        "menu.hyperiumtip8",
        "menu.hyperiumtip9",
        "menu.hyperiumtip10",
        "menu.hyperiumtip11"
    ));

    private Random random = new Random();
    private String selectedTip = hyperiumTips.get(random.nextInt(hyperiumTips.size()));

    public GuiHyperiumScreenMainMenu() {
        if (Minecraft.getMinecraft().isFullScreen() && Settings.WINDOWED_FULLSCREEN && FIRST_START) {
            GuiHyperiumScreenMainMenu.FIRST_START = false;
            Minecraft.getMinecraft().toggleFullscreen();
            Minecraft.getMinecraft().toggleFullscreen();
        }

        if (Hyperium.INSTANCE.isFirstLaunch()) new SettingsMigrator().migrate();

        if (AddonCheckerUtil.isUsingQuickplay()) {
            hyperiumTips.add("menu.externalhyperiumtip.quickplay");
        }

        if (AddonCheckerUtil.isUsingMediaMod()) {
            hyperiumTips.add("menu.externalhyperiumtip.mediamod");
        }

        if (AddonCheckerUtil.isUsingParticleMod()) {
            hyperiumTips.add("menu.externalhyperiumtip.particlemod");
        }

        if (AddonCheckerUtil.isUsingArrowTrails()) {
            hyperiumTips.add("menu.externalhyperiumtip.arrowtrail");
        }
    }

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    public void initGui() {
        int center = width / 2;
        widthCredits = fontRendererObj.getStringWidth(createdByTeam);
        widthCreditsRest = width - widthCredits - 2;

        buttonList.add(new GuiButton(0, center - 100, getRowPos(2), I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(1, center - 100, getRowPos(3), I18n.format("menu.multiplayer")));
        buttonList.add(hypixelButton = new GuiButton(2, center - 100, getRowPos(4), I18n.format("button.ingame.joinhypixel")));
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
        String creditsString = I18n.format("menu.right");
        drawString(fontRendererObj, creditsString, width - fontRendererObj.getStringWidth(creditsString) - 2, height - 20, -1);
        creditsString = createdByTeam;
        drawString(fontRendererObj, creditsString, width - fontRendererObj.getStringWidth(creditsString) - 2, height - 10, -1);

        if (Settings.HYPERIUM_TIPS && !hyperiumTips.isEmpty()) {
            fontRendererObj.drawSplitString(ChatColor.YELLOW + I18n.format(selectedTip), width / 2 - 200 / 2,
                getRowPos(8), 196, -1);
        }

        // yoinked from 1.12
        if (mouseX > widthCreditsRest && mouseX < widthCreditsRest + widthCredits && mouseY > height - 10 && mouseY < height && Mouse.isInsideWindow()) {
            drawRect(widthCreditsRest, height - 1, widthCreditsRest + widthCredits, height, -1);
        }

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
            hypixelButton.displayString = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ?
                I18n.format("button.ingame.fixhypixelsession") :
                I18n.format("button.ingame.joinhypixel");
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

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX > widthCreditsRest && mouseX < widthCreditsRest + widthCredits && mouseY > height - 10 && mouseY < height) {
            mc.displayGuiScreen(new GuiHyperiumCredits(this));
        }
    }

    private int getRowPos(int rowNumber) {
        return 55 + rowNumber * 23;
    }
}
