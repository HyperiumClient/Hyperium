package cc.hyperium.gui.main;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.*;
import cc.hyperium.installer.InstallerConfig;
import cc.hyperium.installer.utils.DownloadTask;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UpdateUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static cc.hyperium.installer.InstallerFrame.get;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HyperiumMainGui extends HyperiumGui {

    public static HyperiumMainGui INSTANCE = new HyperiumMainGui();
    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
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
    public boolean show = false;
    public UpdateUtils utils;

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

        tabs = Arrays.asList(
                ht,
                cosmeticsTab = new CosmeticsTab(height / 2 - pw * 2, pw),
                settingsTab,
                new AddonsTab(height / 2, pw),
                new InfoTab(height / 2 + pw, pw),
                new AddonsInstallerTab(height / 2 + pw * 2, pw)
        );
        tabFade = 1f;
    }

    @Override
    public void show() {
        super.show();
        pack();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Minecraft.getMinecraft().theWorld == null)
            drawDefaultBackground();

        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res

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

        if(!isLatestVersion() && !show && Settings.UPDATE_NOTIFICATIONS && !Metadata.isDevelopment()) {
                Alert alert = new Alert(Icons.ERROR.getResource(), () -> downloadLatest(), "Hyperium Update! Click here to download.");
            alerts.add(alert);
            show = true;
        }

        if (overlay != null) {
            overlay.render(mouseX, mouseY, width, height);
            int x = width / 6 * 2;
            int y = height / 4;
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

    public boolean isLatestVersion(){
        return Hyperium.INSTANCE.isLatestVersion;
    }

    public void downloadLatest(){
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        JsonHolder vJson = null;
        try {
            vJson = new JsonHolder(get(versions_url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Hyperium.INSTANCE.getNotification().display("Update", "Downloading updates...", 3);
            JsonHolder finalVJson = vJson;
            JsonObject ver = StreamSupport.stream(vJson.optJSONArray("versions").spliterator(), false)
                .filter(v -> finalVJson.optString("latest-stable").equals(v.getAsJsonObject().get("name").getAsString())).findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't find stable version")).getAsJsonObject();
            boolean download = ver.get("install-min").getAsInt() > InstallerConfig.VERSION;
            System.out.println("Download=" + download);
            Multithreading.runAsync(() -> {
                try {
                    File jar;
                    if (download || Hyperium.INSTANCE.isDevEnv()) { // or else it will break in dev env
                        DownloadTask task = new DownloadTask(ver.get("url").getAsString(), System.getProperty("java.io.tmpdir"));
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
            HyperiumMainGui.INSTANCE.currentAlert = null;
        }
    }
}
