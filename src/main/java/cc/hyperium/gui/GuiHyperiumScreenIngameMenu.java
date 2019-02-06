package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.text.DecimalFormat;

public class GuiHyperiumScreenIngameMenu extends GuiHyperiumScreen {

    private static JsonHolder data = new JsonHolder();
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private long lastUpdate = 0L;
    private int cooldown = 0;
    private int baseAngle;

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();
        int i = -16;

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu")));

        /* If Client is on server, add disconnect button */
        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        /* Add initial buttons */
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options")));

        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan")));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats")));

        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();

        buttonList.add(new GuiButton(9, this.width / 2 - 100, height / 4 + 56, 98, 20, I18n.format("button.ingame.hyperiumsettings")));
        buttonList.add(new GuiButton(8, this.width / 2 + 2, height / 4 + 56, 98, 20, I18n.format("button.ingame.hyperiumcredits")));

        WorldClient theWorld = Minecraft.getMinecraft().theWorld;

        // Used to detect if the player is on a singleplayer world or a multiplayer world.
        MinecraftServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (theWorld != null && (integratedServer == null)) {
            GuiButton oldButton = buttonList.remove(3);
            GuiButton newButton = new GuiButton(10, oldButton.xPosition, oldButton.yPosition, oldButton.getButtonWidth(), 20, I18n.format("button.ingame.serverlist"));
            buttonList.add(newButton);
        }

    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                if (Settings.CONFIRM_DISCONNECT) {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiConfirmDisconnect());
                } else {
                    boolean integratedServerRunning = this.mc.isIntegratedServerRunning();
                    button.enabled = false;
                    this.mc.theWorld.sendQuittingDisconnectingPacket();
                    this.mc.loadWorld(null);

                    if (integratedServerRunning) {
                        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMainMenu());
                    } else {
                        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(new GuiMainMenu()));
                    }
                }
                break;

            case 2:
                break;

            case 3:
                break;

            default:
                break;

            case 4:
                mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;

            case 5:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 6:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 7:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiShareToLan(this));
                break;

            case 8:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
                break;

            case 9:
                HyperiumMainGui.INSTANCE.show();
                break;

            case 10:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiIngameMultiplayer(Minecraft.getMinecraft().currentScreen));
                break;

        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();

        GlStateManager.translate(0, height - 50, 0);

        if (System.currentTimeMillis() - lastUpdate > 2000L) {
            refreshData();
        }

        baseAngle %= 360;

        ScaledResolution current = ResolutionUtil.current();
        GlStateManager.translate(current.getScaledWidth() / 2, 5, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount"), 0, -5, 0xFFFFFF);
        GlStateManager.translate(0F, 10F, 0F);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.rotate(baseAngle, 1.0F, 0.0F, 0.0F);
        GlStateManager.enableAlpha();

        float z = 4F;
        float e = 80;
        float i = 0;

        GlStateManager.translate(0.0F, 0.0F, z);

        if (baseAngle < e) {
            i = (e - Math.abs(baseAngle)) / e;
        } else if (baseAngle > 360 - e) {
            i = (e - (Math.abs((360) - baseAngle))) / e;
        }

        if (i > 0) {
            drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.now", ChatFormatting.GREEN + formatter.format(data.optInt("online")) + ChatFormatting.RESET), 0, 0, 0xFFFFFF);
        }

        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(270 - baseAngle)) / e;

        if (i > 0) {
            drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.lastday", ChatFormatting.GREEN + formatter.format(data.optInt("day")) + ChatFormatting.RESET), 0, 0, 0xFFFFFF);
        }

        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(180 - baseAngle)) / e;

        if (i > 0) {
            drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.lastweek", ChatFormatting.GREEN + formatter.format(data.optInt("week")) + ChatFormatting.RESET), 0, 0, 0xFFFFFF);
        }

        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(90 - baseAngle)) / e;

        if (i > 0) {
            drawCenteredString(fontRendererObj, I18n.format("gui.ingamemenu.playercount.alltime", ChatFormatting.GREEN + formatter.format(data.optInt("all")) + ChatFormatting.RESET), 0, 0, 0xFFFFFF);
        }

        GlStateManager.popMatrix();

    }

    private synchronized void refreshData() {

        lastUpdate = System.currentTimeMillis() * 2;

        Multithreading.runAsync(() -> {
            data = PurchaseApi.getInstance().get("https://api.hyperium.cc/users");
            lastUpdate = System.currentTimeMillis();
        });

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        cooldown++;
        if (cooldown > 40) {
            baseAngle += 9;
            if (cooldown >= 50) {
                cooldown = 0;
            }
        }
    }
}
