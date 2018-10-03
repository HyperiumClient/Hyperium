package cc.hyperium.gui.main;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.AddonsInstallerTab;
import cc.hyperium.gui.main.tabs.AddonsTab;
import cc.hyperium.gui.main.tabs.CosmeticsTab;
import cc.hyperium.gui.main.tabs.HomeTab;
import cc.hyperium.gui.main.tabs.InfoTab;
import cc.hyperium.gui.main.tabs.KeybindsTab;
import cc.hyperium.gui.main.tabs.ModsTab;
import cc.hyperium.gui.main.tabs.SettingsTab;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.installer.api.entities.InstallerManifest;
import cc.hyperium.installer.api.entities.VersionManifest;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.DownloadTask;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.InstallerUtils;
import cc.hyperium.utils.UpdateUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/*
 * Created by Cubxity on 20/05/2018
 */
public class MainHyperiumMainGui extends HyperiumGui {

    public static MainHyperiumMainGui INSTANCE = new MainHyperiumMainGui();
    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    public boolean show = false;
    public UpdateUtils utils;
    private long lastSelectionChange = 0L;
    private List<String> loadedAlerts = new ArrayList<>();
    private AbstractTab currentTab = null;
    private Queue<Alert> alerts = new ArrayDeque<>();
    private Alert currentAlert;
    private HyperiumOverlay overlay;
    private float tabFade;
    private float highlightScale = 0f;
    private List<AbstractTab> tabs = new ArrayList<>();
    private CosmeticsTab cosmeticsTab;
    private List<Object> settingsObjects = new ArrayList<>();
    private ModsTab modsTab;

    public MainHyperiumMainGui() {
        try {
            settingsObjects.add(Settings.INSTANCE);
            if (Hyperium.INSTANCE.getModIntegration() == null) {
                GeneralChatHandler.instance().sendMessage("Something went really wrong while loading...");
                return;
            }
            settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutotip());
            settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig());
            settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoTPA().getConfig());
            settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getMotionBlur());
            settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getLevelhead().getConfig());
        } catch (Exception e) {
            e.printStackTrace();
            GeneralChatHandler.instance().sendMessage("Something went wrong while setting up the GUI");
        }
    }

    public List<AbstractTab> getTabs() {
        return tabs;
    }

    public long getLastSelectionChange() {
        return lastSelectionChange;
    }

    public HyperiumFontRenderer getFr() {
        return fr;
    }

    public Queue<Alert> getAlerts() {
        return alerts;
    }

    public List<String> getLoadedAlerts() {
        return loadedAlerts;
    }

    public void setTab(AbstractTab tab) {
        currentTab = tab;
    }

    public CosmeticsTab getCosmeticsTab() {
        return cosmeticsTab;
    }

    @Override
    protected void pack() {
        for (AbstractTab tab : tabs) {
            EventBus.INSTANCE.unregister(tab);
        }
        scollMultiplier = .5;
        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res
        AbstractTab ht = new HomeTab(height / 2 - (pw * 3), pw);
        if (currentTab == null)
            currentTab = ht; // Home tab should be selected one by default

        SettingsTab settingsTab = new SettingsTab(height / 2 - pw, pw);
        EventBus.INSTANCE.register(settingsTab);

        modsTab = new ModsTab(height / 2, pw);
        tabs = Arrays.asList(
                ht,
                cosmeticsTab = new CosmeticsTab(height / 2 - pw * 2, pw),
                settingsTab,
                modsTab,
                new AddonsTab(height / 2 + pw, pw),
                new InfoTab(height / 2 + (pw * 2), pw),
                new KeybindsTab(height / 2 + (pw * 3), pw)
                //new AddonsInstallerTab(height / 2 + (pw * 3), pw) ,
        );
        tabFade = 1f;
    }

    @Override
    public void show() {
        super.show();
        pack();
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Minecraft.getMinecraft().theWorld == null)
            drawDefaultBackground();

        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res

        GlStateManager.scale(3, 3, 0);
        ScaledResolution current = ResolutionUtil.current();
        fontRendererObj.drawString(currentTab.getTitle(), current.getScaledWidth() / 2 / 3 - fontRendererObj.getStringWidth(currentTab.getTitle()) / 2, 15 / 3, Color.WHITE.getRGB(), true);
        GlStateManager.scale(1 / 3F, 1 / 3F, 1 / 3F);
        // Draws side pane
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(-tabFade * pw, 0, 0);
        tabs.forEach(AbstractTab::drawTabIcon);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        currentTab.drawHighlight(1 - highlightScale);

        drawRect(pw * 2, pw, width - pw * 2, height - pw, new Color(0, 0, 0, currentTab instanceof HomeTab ? 110 : 70).getRGB());

        currentTab.draw(mouseX, mouseY, pw * 2, pw, width - pw * 4, height - pw * 2);

        if (!alerts.isEmpty() && currentAlert == null)
            currentAlert = alerts.poll();

        if (currentAlert != null)
            currentAlert.render(fr, width, height);

        if (!isLatestVersion() && !show && Settings.UPDATE_NOTIFICATIONS && !Metadata.isDevelopment()) {
            Alert alert = new Alert(Icons.ERROR.getResource(), () -> downloadLatest(), I18n.format("alert.update.message"));
            alerts.add(alert);
            show = true;
        }

        if (overlay != null) {
            int y = height / 4;


            overlay.render(mouseX, mouseY, width, height);
            int x = width / 6 * 2;
            GlStateManager.scale(2, 2, 2);
            int stringWidth = fontRendererObj.getStringWidth(overlay.getName());
            int x1 = ResolutionUtil.current().getScaledWidth() / 4 - stringWidth / 2;
            int y1 = y / 2 - 10;
            Gui.drawRect(x1 - 1, y1 - 1, x1 + stringWidth + 1, y1 + 9, new Color(30, 30, 30, 255).getRGB());
            fontRendererObj.drawString(overlay.getName(), x1, y1, Color.WHITE.getRGB(), true);

            GlStateManager.scale(.5, .5, .5);
            Icons.EXIT.bind();
            Gui.drawRect(x, y - 16, x + 16, y, new Color(0, 0, 0, 100).getRGB());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(x, y - 16, 0, 0, 144, 144, 16, 16, 144, 144);
        }
        if (tabFade > 0f)
            tabFade -= 0.1f;
        else tabFade = 0f;

        if (tabFade == 0f && highlightScale < 1f)
            highlightScale += 0.08f;
    }

    public boolean isLatestVersion() {
        return Hyperium.INSTANCE.isLatestVersion;
    }

    public void downloadLatest() {
        try {
            Hyperium.INSTANCE.getNotification().display("Update", "Downloading updates...", 3);
            InstallerManifest manifest = InstallerUtils.getManifest();
            VersionManifest ver = manifest.getVersions()[0];
            boolean download = ver.getId() > Metadata.getVersionID();
            System.out.println("Download=" + download);
            Multithreading.runAsync(() -> {
                try {
                    File jar;
                    if (download || Hyperium.INSTANCE.isDevEnv()) { // or else it will break in dev env
                        DownloadTask task = new DownloadTask(ver.getUrl(), System.getProperty("java.io.tmpdir"));
                        task.execute();
                        task.get();
                        jar = new File(System.getProperty("java.io.tmpdir"), task.getFileName());
                    } else {
                        jar = new File(System.getProperty("sun.java.command").split(" ")[0]);
                    }
                    Multithreading.schedule(() -> {
                        Minecraft.getMinecraft().shutdown();
                        try {
                            Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/java -jar " + jar.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, 10, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (overlay == null)
            tabs.stream().filter(t -> {
                GuiBlock block = t.getBlock();
                if (block != null)
                    return block.isMouseOver(mouseX, mouseY)
                            && !(t instanceof AddonsInstallerTab);
                return false;
            }).findFirst().ifPresent(
                    t -> {
                        currentTab = t;
                        highlightScale = 0f;
                    });
        if (mouseButton == 0)
            if (currentAlert != null && width / 4 <= mouseX && height - 20 <= mouseY && width - 20 - width / 4 >= mouseX)
                currentAlert.runAction();
            else if (currentAlert != null && mouseX >= width - 20 - width / 4 && mouseX <= width - width / 4 && mouseY >= height - 20)
                currentAlert.dismiss();

        if (overlay != null) {
            int x = width / 6 * 2;
            int y = height / 4;
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y - 16 && mouseY <= y) {
                setOverlay(null);
            } else
                overlay.mouseClicked();
        }
        if (currentTab != null) {
            currentTab.mouseClicked(mouseX, mouseY);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        currentTab.handleMouseInput();
        if (overlay != null)
            overlay.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (overlay != null && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            overlay.reset();
            overlay = null;
        } else
            super.handleKeyboardInput();
    }

    public HyperiumOverlay getOverlay() {
        return overlay;
    }

    public void setOverlay(HyperiumOverlay overlay) {
        this.overlay = overlay;
        lastSelectionChange = System.currentTimeMillis();
    }

    public AbstractTab getCurrentTab() {
        return currentTab;
    }

    public AbstractTab getModsTab() {
        return modsTab;
    }

    /**
     * Important alerts and announcements from Hyperium team
     */
    public static class Alert {
        private ResourceLocation icon;
        private Runnable action;
        private String title;
        private int step = 0;

        public Alert(ResourceLocation icon, Runnable action, String title) {
            this.icon = icon;
            this.action = action;
            this.title = title;
        }

        protected void render(HyperiumFontRenderer fr, int width, int height) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, (20 - step), 0);
            drawRect(width / 4, height - 20, width - width / 4, height, new Color(0, 0, 0, 40).getRGB());
            fr.drawString(title, width / 4 + 20, height - 20 + (20 - fr.FONT_HEIGHT) / 2, 0xffffff);
            if (icon != null) {
                GlStateManager.enableBlend();
                GlStateManager.color(1f, 1f, 1f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
                drawScaledCustomSizeModalRect(width / 4 + 2, height - 18, 0, 0, 144, 144, 16, 16, 144, 144);
                GlStateManager.disableBlend();
            }
            Icons.CLOSE.bind();
            drawScaledCustomSizeModalRect(width - width / 4 - 18, height - 18, 0, 0, 144, 144, 16, 16, 144, 144);
            GlStateManager.popMatrix();

            if (step != 20)
                step++;
        }

        void runAction() {
            if (action != null)
                action.run();
        }

        void dismiss() {
            MainHyperiumMainGui.INSTANCE.currentAlert = null;
        }
    }
}
