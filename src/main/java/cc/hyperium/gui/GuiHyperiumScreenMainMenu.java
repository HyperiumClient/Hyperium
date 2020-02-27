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
import cc.hyperium.gui.buttons.IconButton;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.tips.TipRegistry;
import cc.hyperium.gui.util.CreateServerButton;
import cc.hyperium.handlers.handlers.SettingsMigrator;
import cc.hyperium.optifine.Reflector;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class GuiHyperiumScreenMainMenu extends GuiHyperiumScreen implements GuiYesNoCallback {

    private static boolean FIRST_START = true;
    private final String createdByTeam = I18n.format("menu.credits");
    private final TipRegistry tipRegistry = TipRegistry.INSTANCE;
    private int widthCredits;
    private int widthCreditsRest;

    private String selectedTip;
    private Random random = new Random();
    private int ticksUntilNewTip;

    private HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Raleway", 64.0F);

    public GuiHyperiumScreenMainMenu() {
        if (Minecraft.getMinecraft().isFullScreen() && Settings.WINDOWED_FULLSCREEN && FIRST_START) {
            GuiHyperiumScreenMainMenu.FIRST_START = false;
            Minecraft.getMinecraft().toggleFullscreen();
            Minecraft.getMinecraft().toggleFullscreen();
        }

        if (Hyperium.INSTANCE.isFirstLaunch()) new SettingsMigrator().migrate();

        tipRegistry.registerTips(
                "menu.hyperiumtip.1",
                "menu.hyperiumtip.2",
                "menu.hyperiumtip.3",
                "menu.hyperiumtip.4",
                "menu.hyperiumtip.5",
                "menu.hyperiumtip.6",
                "menu.hyperiumtip.7",
                "menu.hyperiumtip.8",
                "menu.hyperiumtip.9",
                "menu.hyperiumtip.10",
                "menu.hyperiumtip.11",
                "menu.hyperiumtip.12",
                "menu.hyperiumtip.13"
        );

        selectedTip = tipRegistry.getTips().get(random.nextInt(tipRegistry.getTips().size()));
    }

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;
        widthCredits = fontRendererObj.getStringWidth(createdByTeam);
        widthCreditsRest = width - widthCredits - 2;

        buttonList.add(new GuiButton(0, centerX - 100, centerY - 50, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(1, centerX - 100, centerY - 28, I18n.format("menu.multiplayer")));
        buttonList.add(serverButton = new GuiButton(2, centerX - 100, centerY - 6, I18n.format("button.ingame.joinhypixel")));
        buttonList.add(new GuiButton(3, centerX - 100, centerY + 16, I18n.format("button.ingame.hyperiumsettings")));
        buttonList.add(new GuiButton(4, centerX - 100, centerY + 38, 98, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(5, centerX + 2, centerY + 38, 98, 20, I18n.format("menu.quit")));
        buttonList.add(new IconButton(6, 3, 3, 16, 16, I18n.format("button.menu.cosmeticshop"),
                new ResourceLocation("hyperium", "textures/material/shopping_bag.png")));
        buttonList.add(new IconButton(7, 23, 3, 16, 16, I18n.format("button.menu.changebackground"),
                new ResourceLocation("hyperium", "textures/material/gallery.png")));
        buttonList.add(new IconButton(8, 43, 3, 16, 16, I18n.format("button.menu.accountswitch"),
                new ResourceLocation("hyperium", "textures/material/account.png")));
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

        int versionOffset = Reflector.OF_VERSION.exists() ? 20 : 10;
        if (versionOffset == 20) {
            fontRendererObj.drawStringWithShadow("OptiFine " + Reflector.OF_VERSION.get(null).substring(9), 3, height - 10, -1);
        }
        fontRendererObj.drawStringWithShadow("Hyperium " + Metadata.getVersion(), 3, height - versionOffset, -1);
        String creditsString = I18n.format("menu.right");
        drawString(fontRendererObj, creditsString, width - fontRendererObj.getStringWidth(creditsString) - 2, height - 20, -1);
        creditsString = createdByTeam;
        drawString(fontRendererObj, creditsString, width - fontRendererObj.getStringWidth(creditsString) - 2, height - 10, -1);

        if (Settings.HYPERIUM_TIPS && !tipRegistry.getTips().isEmpty()) {
            fontRendererObj.drawSplitString(ChatColor.YELLOW + I18n.format(selectedTip), width / 2 - 200 / 2,
                    height / 2 + 62, 200, -1);
        }

        // yoinked from 1.12
        if (mouseX > widthCreditsRest && mouseX < widthCreditsRest + widthCredits && mouseY > height - 10 && mouseY < height && Mouse.isInsideWindow()) {
            drawRect(widthCreditsRest, height - 1, widthCreditsRest + widthCredits, height, -1);
        }

        if (PurchaseApi.getInstance() != null && PurchaseApi.getInstance().getSelf() != null) {
            JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
            int credits = response.optInt("remaining_credits");

            fontRendererObj.drawStringWithShadow(mc.getSession().getUsername() + " - " +
                    I18n.format("menu.profile.credits", credits), 3, height - (versionOffset + 10), 0xFFFF00);
        }

        fontRenderer.drawCenteredString(Metadata.getModid(), width / 2F, (height >> 1) - 95, new Color(0, 0, 0, 150).getRGB());
        fontRenderer.drawCenteredString(Metadata.getModid(), width / 2F, (height >> 1) - 96, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GuiButton serverButton = this.serverButton;

        if (serverButton != null) {
            serverButton.displayString = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) ?
                    I18n.format("gui.serverjoin.customizeserver") :
                    Settings.SERVER_BUTTON_NAME;
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
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
                if (!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))) {
                    GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
                    guiMultiplayer.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
                    guiMultiplayer.makeDirectConnect();
                    ServerData data = new ServerData("customServer", Settings.SERVER_IP, false);
                    guiMultiplayer.setIp(data);
                    guiMultiplayer.confirmClicked(true, 0);
                    Hyperium.INSTANCE.setFromMainMenu(true);
                } else {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new CreateServerButton(this));
                }
                break;


            case 3:
                HyperiumMainGui.INSTANCE.setTab(0);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumMainGui.INSTANCE);
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

            case 8:
                Hyperium.INSTANCE.getModIntegration().getAccountSwitcher().displayGui(this);
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

    @Override
    public void updateScreen() {
        super.updateScreen();
        ticksUntilNewTip++;

        if (ticksUntilNewTip > 200) {
            ticksUntilNewTip = 0;
            selectedTip = tipRegistry.getTips().get(random.nextInt(tipRegistry.getTips().size()));
        }
    }
}
