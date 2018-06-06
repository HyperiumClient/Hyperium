package cc.hyperium.gui.main;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.AddonsTab;
import cc.hyperium.gui.main.tabs.HomeTab;
import cc.hyperium.gui.main.tabs.InfoTab;
import cc.hyperium.gui.main.tabs.SettingsTab;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
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
    private static List<String> loadedAlerts = new ArrayList<>(); // so old alerts does not shot again
    private static final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    private static AbstractTab currentTab = null; // static so it is still the same tab
    private static Queue<Alert> alerts = new ArrayDeque<>(); // static so alert does not disappear until user dismiss it
    private static Alert currentAlert;
    private HyperiumOverlay overlay;
    private float tabFade;
    private float highlightScale = 0f;

    private List<AbstractTab> tabs;

    public static HyperiumFontRenderer getFr() {
        return fr;
    }

    public void setOverlay(HyperiumOverlay overlay) {
        this.overlay = overlay;
    }

    @Override
    protected void pack() {
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
                new InfoTab(height / 2 + pw, pw)
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
        GlStateManager.translate(- tabFade * pw, 0, 0);
        tabs.forEach(AbstractTab::drawTabIcon);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        currentTab.drawHighlight(1 - highlightScale);

        drawRect(pw * 2, pw, width - pw * 2, height - pw, new Color(0, 0, 0, 70).getRGB());

        currentTab.draw(mouseX, mouseY, pw * 2, pw, width - pw * 4, height - pw);

        if (!alerts.isEmpty() && currentAlert == null)
            currentAlert = alerts.poll();

        if (currentAlert != null)
            currentAlert.render(fr, width, height);

        if (overlay != null)
            overlay.render(mouseX, mouseY, width, height);
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
            tabs.stream().filter(t -> t.getBlock().isMouseOver(mouseX, mouseY)).findFirst().ifPresent(
                    t -> {
                        currentTab = t;
                        highlightScale = 0f;
                    });
        if (mouseButton == 0)
            if (currentAlert != null && width / 4 <= mouseX && height - 20 <= mouseY && width - 20 - width / 4 >= mouseX)
                currentAlert.runAction();
            else if (currentAlert != null && mouseX >= width - 20 - width / 4 && mouseX <= width - width / 4 && mouseY >= height - 20)
                currentAlert.dismiss();
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

    /**
     * Important alerts and announcements from Hyperium team
     */
    public static class Alert {
        private ResourceLocation icon;
        private Runnable action;
        private String title;

        public Alert(ResourceLocation icon, Runnable action, String title) {
            this.icon = icon;
            this.action = action;
            this.title = title;
        }

        protected void render(HyperiumFontRenderer fr, int width, int height) {
            drawRect(width / 4, height - 20, width - width / 4, height, new Color(0, 0, 0, 40).getRGB());
            fr.drawString(title, width / 4 + 20, height - 20 + (20 - fr.FONT_HEIGHT) / 2, 0xffffff);


            //GlStateManager.scale(1f, 1f, 1f );
            if (icon != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.color(1f, 1f, 1f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
                drawScaledCustomSizeModalRect(width / 4 + 2, height - 18, 0, 0, 144, 144, 16, 16, 144, 144);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            //TODO: Dismiss icon
        }

        void runAction() {
            action.run();
        }

        void dismiss() {
            currentAlert = null;
        }
    }

    public static Queue<Alert> getAlerts() {
        return alerts;
    }

    public static List<String> getLoadedAlerts() {
        return loadedAlerts;
    }
}
