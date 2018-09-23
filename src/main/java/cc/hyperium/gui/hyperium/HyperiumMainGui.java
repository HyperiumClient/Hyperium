package cc.hyperium.gui.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Category;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.tabs.NewsTab;
import cc.hyperium.gui.hyperium.tabs.SettingsTab;
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
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public HyperiumMainGui() {
        smol = new HyperiumFontRenderer(Settings.GUI_FONT, 14.0F, 0, 1.0F);
        font = new HyperiumFontRenderer(Settings.GUI_FONT, 16.0F, 0, 1.0F);
        title = new HyperiumFontRenderer(Settings.GUI_FONT, 30.0F, 0, 1.0F);
        settingsObjects.add(Settings.INSTANCE);
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutotip());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoTPA().getConfig());
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
                new NewsTab(this)
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

    @Override
    protected void pack() {
        Method loadShaderMethod = null;
        try {
            loadShaderMethod = ReflectionUtil.findDeclaredMethod(EntityRenderer.class, new String[] {"loadShader", "a"}, ResourceLocation.class);
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
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid

        if (Minecraft.getMinecraft().theWorld == null) {
            renderHyperiumBackground(ResolutionUtil.current());
        }

        // Header
        drawRect(xg, yg, xg * 10, yg * 2, 0x64000000);
        drawRect(xg, yg * 2, xg * 10, yg * 9, 0x28000000);
        GlStateModifier.INSTANCE.reset();
        Icons.ARROW_RIGHT.bind();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(xg * 2, yg * 2, 0);
        GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 144, 144, yg, yg, 144, 144);
        GlStateManager.popMatrix();

        Icons.ARROW_RIGHT.bind();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(xg * 9, yg, 0);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 144, 144, yg, yg, 144, 144);
        GlStateManager.popMatrix();

        title.drawCenteredString(currentTab.getTitle().toUpperCase(), width / 2F, yg + (yg / 2F - 8), 0xffffff);

        // Body
        currentTab.render(xg, yg * 2, xg * 9, yg * 8);

        // Footer
        smol.drawString(Metadata.getVersion(), width - smol.getWidth(Metadata.getVersion()) - 1, height - 10, 0xffffffff);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);   // X grid

        if (mouseY >= yg && mouseY <= yg * 2) {
            int size = tabs.size();
            int i = tabs.indexOf(currentTab);
            if (mouseX > xg && mouseX < xg * 4) {
                i--;
                if (i < 0)
                    i = size - 1;
                setTab(i);
            }
            if (mouseX > xg * 7 && mouseX < xg * 10) {
                i++;
                if (i > size - 1)
                    i = 0;
                setTab(i);
            }
        }
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
}
