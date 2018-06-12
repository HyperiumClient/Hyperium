package cc.hyperium.gui.main;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.*;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Queue;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HyperiumMainGui extends HyperiumGui {

    public static HyperiumMainGui INSTANCE;
    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    private long lastSelectionChange = 0L;
    private List<String> loadedAlerts = new ArrayList<>();
    private AbstractTab currentTab = null;
    private Queue<Alert> alerts = new ArrayDeque<>();
    private Alert currentAlert;
    private HyperiumOverlay overlay;
    private float tabFade;
    private float highlightScale = 0f;
    private List<AbstractTab> tabs;

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

    @Override
    protected void pack() {
        scollMultiplier = .5;
        INSTANCE = this;
        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res
        AbstractTab ht = new HomeTab(height / 2 - (pw * 2), pw);
        if (currentTab == null)
            currentTab = ht; // Home tab should be selected one by default
        tabs = Arrays.asList(
                ht,
                new SettingsTab(height / 2 - pw, pw),
                new AddonsTab(height / 2, pw),
                new InfoTab(height / 2 + pw, pw),
                new AddonsInstallerTab(height / 2 + pw * 2, pw)
        );
        tabFade = 1f;
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

        drawRect(pw * 2, pw, width - pw * 2, height - pw, new Color(0, 0, 0, 70).getRGB());

        currentTab.draw(mouseX, mouseY, pw * 2, pw, width - pw * 4, height - pw * 2);

        if (!alerts.isEmpty() && currentAlert == null)
            currentAlert = alerts.poll();

        if (currentAlert != null)
            currentAlert.render(fr, width, height);

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

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (overlay == null)
            tabs.stream().filter(t -> t.getBlock().isMouseOver(mouseX, mouseY) && !(t instanceof AddonsInstallerTab)).findFirst().ifPresent(
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
//        currentTab.handleMouseInput();
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
            GlStateManager.popMatrix();

            if (step != 20)
                step++;

            //TODO: Dismiss icon
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
