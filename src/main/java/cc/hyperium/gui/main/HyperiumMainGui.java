package cc.hyperium.gui.main;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.font.Fonts;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.tabs.HomeTab;
import cc.hyperium.gui.main.tabs.InfoTab;
import cc.hyperium.gui.main.tabs.SettingsTab;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HyperiumMainGui extends HyperiumGui {
    private static AbstractTab currentTab = null; // static so it is still the same tab
    private static Queue<Alert> alerts = new ArrayDeque<>(); // static so alert does not disappear until user dismiss it
    private static Alert currentAlert;

    private List<AbstractTab> tabs;
    private HyperiumFontRenderer fr;

    {
        try {
            fr = new HyperiumFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream("/assets/hyperium/fonts/SegoeUI-Light.ttf")), 0);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            fr = Fonts.ARIAL.getTrueTypeFont(); // backup
        }
    }

    static {
        alerts.add(new Alert(null, () -> {
            System.out.println("Alert clicked!");
        }, "Test alert pls click kthx"));
    }

    @Override
    protected void pack() {
        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res
        AbstractTab ht = new HomeTab(height / 2 - (pw * 2), pw);
        if (currentTab == null)
            currentTab = ht; // Home tab should be selected one by default
        tabs = Arrays.asList(
                ht,
                new SettingsTab(height / 2 - pw, pw),
                new InfoTab(height / 2 - pw, pw)
        );
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Draws side pane
        currentTab.drawHighlight();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        tabs.forEach(AbstractTab::drawTabIcon);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        int pw = width / 15;
        if (pw > 144)
            pw = 144; // icon res

        drawRect(pw * 2, pw, width - pw * 2, height - pw, new Color(0, 0, 0, 70).getRGB());

        currentTab.draw(mouseX, mouseY, pw * 2, pw, width - pw, height - pw);

        if (!alerts.isEmpty() && currentAlert == null)
            currentAlert = alerts.poll();

        if (currentAlert != null)
            currentAlert.render(fr, width, height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        tabs.stream().filter(t -> t.getBlock().isMouseOver(mouseX, mouseY)).findFirst().ifPresent(
                t -> currentTab = t);
        if (mouseButton == 0)
            if (currentAlert != null && width / 4 <= mouseX && height - 30 <= mouseY && width - 30 - width / 4 >= mouseX)
                currentAlert.runAction();
        //TODO: Dismiss
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        tabs.forEach(AbstractTab::handleMouseInput);
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
            drawRect(width / 4, height - 30, width - width / 4, height, new Color(0, 0, 0, 40).getRGB());
            fr.drawString(title, width / 4 + 30, height - 30 + (30 - fr.FONT_HEIGHT) / 2, 0xffffff);

            //TODO: Render icon
            //TODO: Dismiss icon
        }

        void runAction() {
            action.run();
            dismiss();
        }

        void dismiss() {

        }
    }
}
