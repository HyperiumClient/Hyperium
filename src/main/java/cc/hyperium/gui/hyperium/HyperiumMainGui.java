package cc.hyperium.gui.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Category;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.MaterialTextField;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.tabs.NewsTab;
import cc.hyperium.gui.hyperium.tabs.SettingsTab;
import cc.hyperium.gui.hyperium.tabs.UpdateTab;
import cc.hyperium.handlers.handlers.SettingsHandler;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by Cubxity on 27/08/2018
 */
public class HyperiumMainGui extends HyperiumGui {

    public static HyperiumMainGui INSTANCE = new HyperiumMainGui();
    private static int tabIndex = 0; // save tab position
    private int initialGuiScale;
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();

    private HyperiumFontRenderer smol;
    private HyperiumFontRenderer font;
    private HyperiumFontRenderer title;
    private List<AbstractTab> tabs;
    private AbstractTab currentTab;
    private List<RGBFieldSet> rgbFields = new ArrayList<>();

    private Alert currentAlert;
    public boolean show = false;

    private MaterialTextField searchField;

    private Queue<Alert> alerts = new ArrayDeque<>();

    public HyperiumMainGui() {
        smol = new HyperiumFontRenderer(Settings.GUI_FONT, 14.0F, 0, 1.0F);
        font = new HyperiumFontRenderer(Settings.GUI_FONT, 16.0F, 0, 1.0F);
        title = new HyperiumFontRenderer(Settings.GUI_FONT, 30.0F, 0, 1.0F);
        settingsObjects.add(Settings.INSTANCE);
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutotip());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getMotionBlur());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getLevelhead().getConfig());
        SettingsHandler settingsHandler = Hyperium.INSTANCE.getHandlers().getSettingsHandler();
        settingsObjects.addAll(settingsHandler.getSettingsObjects());
        HashMap<Field, List<Consumer<Object>>> call1 = settingsHandler.getcallbacks();
        for (Field field : call1.keySet()) {
            callbacks.computeIfAbsent(field, tmp -> new ArrayList<>()).addAll(call1.get(field));
        }

        HashMap<Field, Supplier<String[]>> customStates = settingsHandler.getCustomStates();
        for (Field field : customStates.keySet()) {
            this.customStates.put(field, customStates.get(field));
        }
        try {
            rgbFields.add(new RGBFieldSet(Settings.class.getDeclaredField("REACH_RED"),
                    Settings.class.getDeclaredField("REACH_GREEN"),
                    Settings.class.getDeclaredField("REACH_BLUE"), Category.REACH, true, Settings.INSTANCE));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        initialGuiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        // Adjust if GUI scale is on automatic.
        if (Minecraft.getMinecraft().gameSettings.guiScale == 0)
            Minecraft.getMinecraft().gameSettings.guiScale = 3;

        tabs = Arrays.asList(
                new SettingsTab(this),
                new NewsTab(this),
                new UpdateTab(this)
        );
        guiScale = 2;
        scollMultiplier = 2;
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

    public Queue<Alert> getAlerts() {
        return alerts;
    }

    @Override
    protected void pack() {
        Method loadShaderMethod = null;
        try {
            loadShaderMethod = ReflectionUtil.findDeclaredMethod(EntityRenderer.class, new String[]{"loadShader", "a"}, ResourceLocation.class);
        } catch (Exception ignored) {
        }

        if (loadShaderMethod != null) {
            loadShaderMethod.setAccessible(true);
            try {
                loadShaderMethod.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("shaders/hyperium_blur.json"));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        show = false;
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid
        searchField = new MaterialTextField(xg * 10 - 110, yg + (yg / 2 - 10), 100, 20, I18n.format("tabs.searchbar"), font);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid

        if (Minecraft.getMinecraft().theWorld == null) {
            renderHyperiumBackground(ResolutionUtil.current());
        }

        /* Render shadowed bar at top of screen */
        if(Minecraft.getMinecraft().theWorld == null) {
            this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        }
        /* Render Header */
        drawRect(xg, yg, xg * 10, yg * 2, 0x64000000);
        drawRect(xg, yg * 2, xg * 10, yg * 9, 0x28000000);
        searchField.render(mouseX, mouseY);
        GlStateModifier.INSTANCE.reset();

        title.drawCenteredString(I18n.format(currentTab.getTitle()), this.width / 2, yg + (yg / 2 - 8), 0xFFFFFF);

        /* Render Body */
        currentTab.setFilter(searchField.getText().isEmpty() ? null : searchField.getText());
        currentTab.render(xg, yg * 2, xg * 9, yg * 7);

        /* Render Footer */
        smol.drawString(Metadata.getVersion(), width - smol.getWidth(Metadata.getVersion()) - 1, height - 10, 0xffffffff);

        /* Render Tab Switcher */
        Icons.ARROW_LEFT.bind();
        GlStateManager.pushMatrix();
        Gui.drawScaledCustomSizeModalRect(width / 2 - xg, yg * 9, 0, 0, 144, 144, yg / 2, yg / 2, 144, 144);
        Icons.ARROW_RIGHT.bind();
        Gui.drawScaledCustomSizeModalRect(width / 2 + xg - (yg / 2), yg * 9, 0, 0, 144, 144, yg / 2, yg / 2, 144, 144);
        GlStateManager.popMatrix();

        // Alerts
        if (!alerts.isEmpty() && currentAlert == null)
            currentAlert = alerts.poll();

        if (currentAlert != null)
            currentAlert.render(font, width, height);

        if (!isLatestVersion() && !show && Settings.UPDATE_NOTIFICATIONS && !Metadata.isDevelopment() && !((UpdateTab) tabs.get(2)).isBusy()) {
            System.out.println("Sending alert...");
            Alert alert = new Alert(Icons.ERROR.getResource(), () -> setTab(2), I18n.format("alert.update.message"));
            alerts.add(alert);
            show = true;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        searchField.update();
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
                if (i < 0)
                    i = size - 1;
                setTab(i);
            }
            ix = width / 2 + xg / 2;
            if (mouseX >= ix && mouseX <= ix + xg / 2) {
                i++;
                if (i > size - 1)
                    i = 0;
                setTab(i);
            }
        }

        if (mouseButton == 0) {
            if (currentAlert != null && width / 4 <= mouseX && height - 20 <= mouseY && width - 20 - width / 4 >= mouseX)
                currentAlert.runAction();
            else if (currentAlert != null && mouseX >= width - 20 - width / 4 && mouseX <= width - width / 4 && mouseY >= height - 20)
                currentAlert.dismiss();
        }
        searchField.onClick(mouseX, mouseY, mouseButton);
    }

    private void renderHyperiumBackground(ScaledResolution sr) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();

        if (Settings.BACKGROUND.equalsIgnoreCase("default")) {
            drawDefaultBackground();
        } else {
            if (customImage.exists() && bgDynamicTexture != null && customBackground) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(bgDynamicTexture);
            } else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(background);
            }

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0D, (double) sr.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            worldrenderer.pos((double) sr.getScaledWidth(), (double) sr.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            worldrenderer.pos((double) sr.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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

    public boolean isLatestVersion() {
        return Hyperium.INSTANCE.isLatestVersion;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Minecraft.getMinecraft().entityRenderer.stopUseShader();
        if (initialGuiScale == 0) {
            // User was on automatic so return to that scale.
            Minecraft.getMinecraft().gameSettings.guiScale = 0;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        currentTab.handleMouseInput();
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
            INSTANCE.currentAlert = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        searchField.keyTyped(typedChar, keyCode);
    }
}
