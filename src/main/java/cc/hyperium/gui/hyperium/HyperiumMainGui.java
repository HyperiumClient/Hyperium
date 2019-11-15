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

package cc.hyperium.gui.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Category;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.GuiHyperiumScreen;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.MaterialTextField;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.tabs.SettingsTab;
import cc.hyperium.gui.hyperium.tabs.ShopTab;
import cc.hyperium.handlers.handlers.SettingsHandler;
import cc.hyperium.utils.GlStateModifier;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by Cubxity on 27/08/2018
 */
public class HyperiumMainGui extends HyperiumGui {

    public static HyperiumMainGui INSTANCE = new HyperiumMainGui();
    private static int tabIndex; // save tab position
    public boolean show;
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();
    private HyperiumFontRenderer smol;
    private HyperiumFontRenderer font;
    private HyperiumFontRenderer title;
    private HyperiumFontRenderer title2;
    private List<AbstractTab> tabs;
    private AbstractTab currentTab;
    private List<RGBFieldSet> rgbFields = new ArrayList<>();
    private Alert currentAlert;
    private MaterialTextField searchField;
    private Queue<Alert> alerts = new ArrayDeque<>();

    private HyperiumMainGui() {
        smol = new HyperiumFontRenderer(Settings.GUI_FONT, 14.0F);
        font = new HyperiumFontRenderer(Settings.GUI_FONT, 16.0F);
        title = new HyperiumFontRenderer(Settings.GUI_FONT, 30.0F);
        title2 = new HyperiumFontRenderer("roboto medium", 24.0F);
        settingsObjects.add(Settings.INSTANCE);
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutotip());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getMotionBlur());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getLevelhead().getDisplayManager().getMasterConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getGlintColorizer().getColors());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getChunkAnimator().getConfig());
        SettingsHandler settingsHandler = Hyperium.INSTANCE.getHandlers().getSettingsHandler();
        settingsObjects.addAll(settingsHandler.getSettingsObjects());
        HashMap<Field, List<Consumer<Object>>> call1 = settingsHandler.getcallbacks();
        call1.forEach((key, value) -> callbacks.computeIfAbsent(key, tmp -> new ArrayList<>()).addAll(value));
        HashMap<Field, Supplier<String[]>> customStates = settingsHandler.getCustomStates();
        customStates.forEach((key, value) -> this.customStates.put(key, value));

        try {
            rgbFields.add(new RGBFieldSet(
                Settings.class.getDeclaredField("REACH_RED"),
                Settings.class.getDeclaredField("REACH_GREEN"),
                Settings.class.getDeclaredField("REACH_BLUE"), Category.REACH, true,
                Settings.INSTANCE));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabs = Arrays.asList(
            new SettingsTab(this),
            new ShopTab(this)
        );

        scrollMultiplier = 2;
        setTab(tabIndex);
    }

    public HashMap<Field, Supplier<String[]>> getCustomStates() {
        return customStates;
    }

    public HashMap<Field, List<Consumer<Object>>> getCallbacks() {
        return callbacks;
    }

    public List<RGBFieldSet> getRgbFields() {
        return rgbFields;
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }

    @Override
    protected void pack() {
        show = false;
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid
        searchField = new MaterialTextField(xg * 10 - 110, yg + (yg / 2 - 10), 100, 20,
            I18n.format("tabs.searchbar"), font);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid

        if (Minecraft.getMinecraft().theWorld == null) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlpha();
            GuiHyperiumScreen.renderBackgroundImage();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        /* Render Header */
        drawRect(xg, yg, xg * 10, yg * 2, new Color(0, 0, 0, (int) Settings.SETTINGS_ALPHA).getRGB());
        drawRect(xg, yg * 2, xg * 10, yg * 9, new Color(0, 0, 0, (int) (Settings.SETTINGS_ALPHA / 2)).getRGB());
        searchField.render(mouseX, mouseY);
        GlStateModifier.INSTANCE.reset();

        title2.drawCenteredString(I18n.format(currentTab.getTitle()), width >> 1,
            yg + ((yg >> 1) - 6), 0xFFFFFF);

        /* Render Body */
        currentTab.setFilter(searchField.getText().isEmpty() ? null : searchField.getText());
        currentTab.render(xg, yg * 2, xg * 9, yg * 7);

        /* Render Footer */
        smol.drawString(Metadata.getVersion(), width - smol.getWidth(Metadata.getVersion()) - 1,
            height - 10, 0xffffffff);

        /* Render Tab Switcher */
        Icons.ARROW_LEFT.bind();
        GlStateManager.pushMatrix();
        Gui.drawScaledCustomSizeModalRect(width / 2 - xg, yg * 9, 0, 0, 144, 144, yg / 2, yg / 2,
            144, 144);
        Icons.ARROW_RIGHT.bind();
        Gui.drawScaledCustomSizeModalRect(width / 2 + xg - (yg / 2), yg * 9, 0, 0, 144, 144, yg / 2,
            yg / 2, 144, 144);
        GlStateManager.popMatrix();

        // Alerts
        if (!alerts.isEmpty() && currentAlert == null) currentAlert = alerts.poll();
        if (currentAlert != null) currentAlert.render(font, width, height);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        searchField.update();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid

        if (mouseY >= yg * 9 && mouseY <= yg * 10) {
            int size = tabs.size();
            int i = tabs.indexOf(currentTab);
            int ix = width / 2 - xg;

            if (mouseX >= ix && mouseX <= ix + xg / 2) {
                i--;
                if (i < 0) i = size - 1;
                setTab(i);
            }

            ix = width / 2 + xg / 2;

            if (mouseX >= ix && mouseX <= ix + xg / 2) {
                i++;
                if (i > size - 1) i = 0;
                setTab(i);
            }
        }

        if (mouseButton == 0) {
            if (currentAlert != null && width / 4 <= mouseX && height - 20 <= mouseY && width - 20 - width / 4 >= mouseX) {
                currentAlert.runAction();
            } else if (currentAlert != null && mouseX >= width - 20 - width / 4
                && mouseX <= width - width / 4 && mouseY >= height - 20) {
                currentAlert.dismiss();
            }
        }

        searchField.onClick(mouseX, mouseY, mouseButton);
    }

    public HyperiumFontRenderer getFont() {
        return font;
    }

    public HyperiumFontRenderer getTitle() {
        return title;
    }

    public void setTab(int i) {
        tabIndex = i;
        currentTab = tabs.get(i);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        // Save all settings.
        Hyperium.CONFIG.save();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        currentTab.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        searchField.keyTyped(typedChar, keyCode);
    }

    /**
     * Important alerts and announcements from Hyperium team
     */
    public static class Alert {
        private ResourceLocation icon;
        private Runnable action;
        private String title;
        private int step;

        public Alert(ResourceLocation icon, Runnable action, String title) {
            this.icon = icon;
            this.action = action;
            this.title = title;
        }

        protected void render(HyperiumFontRenderer fr, int width, int height) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, (20 - step), 0);
            drawRect(width / 4, height - 20, width - width / 4, height,
                new Color(0, 0, 0, 40).getRGB());
            fr.drawString(title, (width >> 2) + 20, height - 20 + ((20 - fr.FONT_HEIGHT) >> 1), 0xffffff);

            if (icon != null) {
                GlStateManager.enableBlend();
                GlStateManager.color(1f, 1f, 1f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
                drawScaledCustomSizeModalRect(width / 4 + 2, height - 18, 0, 0, 144, 144, 16, 16,
                    144, 144);
                GlStateManager.disableBlend();
            }

            Icons.CLOSE.bind();
            drawScaledCustomSizeModalRect(width - width / 4 - 18, height - 18, 0, 0, 144, 144, 16,
                16, 144, 144);
            GlStateManager.popMatrix();

            if (step != 20) {
                step++;
            }
        }

        void runAction() {
            if (action != null) {
                action.run();
            }
        }

        void dismiss() {
            INSTANCE.currentAlert = null;
        }
    }
}
