package cc.hyperium.gui;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.playerrenderer.GuiPlayerRenderer;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.JsonHolder;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GuiHyperiumScreen extends GuiScreen {

    public static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static ResourceLocation background = new ResourceLocation("textures/material/backgrounds/1.png");
    public static boolean customBackground = false;
    public static File customImage = new File(Minecraft.getMinecraft().mcDataDir, "customImage.png");
    public static ResourceLocation bgDynamicTexture = null;
    public static BufferedImage bgBr = null;
    public static HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    public static HyperiumFontRenderer sfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    public static int panoramaTimer;
    public static DynamicTexture viewportTexture;
    private static float swing;
    public GuiButton hypixelButton;

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_FASTEST);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) uWidth) * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) (v * f1)).endVertex();
        worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
    }

    public static ResourceLocation getBackground() {
        return background;
    }

    public static void setBackground(ResourceLocation givenBackground) {
        background = givenBackground;
    }

    public static void setCustomBackground(boolean givenBoolean) {
        customBackground = givenBoolean;
    }

    public void initGui() {

        customBackground = Settings.BACKGROUND.equalsIgnoreCase("CUSTOM");
        if (customImage.exists() && customBackground) {
            try {
                bgBr = ImageIO.read(new FileInputStream(customImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bgBr != null)
                bgDynamicTexture = mc.getRenderManager().renderEngine.getDynamicTextureLocation(customImage.getName(), new DynamicTexture(bgBr));
            if (bgDynamicTexture == null)
                return;
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        switch (getStyle()) {
            case DEFAULT:
                drawDefaultStyleScreen(mouseX, mouseY, partialTicks);
                break;
            case HYPERIUM:
                drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
                break;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    public GuiStyle getStyle() {
        return GuiStyle.valueOf(Minecraft.getMinecraft().theWorld == null ? Settings.MENU_STYLE : Settings.PAUSE_STYLE);
    }

    public void renderHyperiumBackground(ScaledResolution p_180476_1_) {

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        if (customImage.exists() && bgDynamicTexture != null && customBackground) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(bgDynamicTexture);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0D, (double) p_180476_1_.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            worldrenderer.pos((double) p_180476_1_.getScaledWidth(), (double) p_180476_1_.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            worldrenderer.pos((double) p_180476_1_.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double) p_180476_1_.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double) p_180476_1_.getScaledWidth(), (double) p_180476_1_.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double) p_180476_1_.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {

        GlStateManager.pushMatrix();

        swing++;

        if (mc.theWorld == null) {
            if (Settings.BACKGROUND.equals("DEFAULT")) {
                GlStateManager.disableAlpha();
                this.renderSkybox(mouseX, mouseY, partialTicks);
                GlStateManager.enableAlpha();
            } else {
                // Background
                GlStateManager.disableAlpha();
                ScaledResolution sr = new ScaledResolution(mc);
                this.renderHyperiumBackground(sr);
                GlStateManager.enableAlpha();

            }
        }

        /* Render shadowed bar at top of screen */
        if (mc.theWorld == null) {
            this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        } else {
            this.drawDefaultBackground();
        }

        this.drawRect(0, 4, width, 55, 0x33000000);
        this.drawRect(0, 5, width, 54, 0x33000000);

        /* Render Client Logo */
        GlStateManager.color(1, 1, 1, 1);
        ResourceLocation logo = new ResourceLocation("textures/hyperium-logo.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(logo);
        drawScaledCustomSizeModalRect(10, 5, 0, 0, 2160, 500, 200, 47, 2160, 500);

        /* Render profile container */
        this.drawRect(width - 155, 10, width - 10, 49, 0x33000000);
        this.drawRect(width - 156, 9, width - 9, 50, 0x33000000);

        /* Render client version container */
        /*
        drawRect(width - 153, 39, width - 60, 48, 0x33000000);
        drawRect(width - 154, 40, width - 59, 49, 0x33000000);
        */

        // Looks weird with the small green strip
        // drawRect(width - 160, 10, width - 158, 40, new Color(149, 201, 144, 255).getRGB());

        /* Fetch player credit count */
        if(!Hyperium.INSTANCE.isDevEnv()) {
            if (PurchaseApi.getInstance() != null && PurchaseApi.getInstance().getSelf() != null) {
                JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
                int credits = response.optInt("total_credits");

                /* Render player credits count and username */
                fr.drawString(Minecraft.getMinecraft().getSession().getUsername(), width - 153, 13, 0xFFFFFF);
                fr.drawString(I18n.format("menu.profile.credits", credits), width - 153, 25, 0xFFFF00);
            }
        }


        float val = (float) (Math.sin(swing / 40) * 30);

        ScissorState.scissor(width - 153, 0, 145, 49, true);
        if(!Hyperium.INSTANCE.isDevEnv()) {
            GuiPlayerRenderer.renderPlayerWithRotation(width - 118, -4, val);
        }
        ScissorState.endScissor();

        /* Render Hyperium version number */
        fr.drawStringScaled("Hyperium v" + (Hyperium.INSTANCE.isLatestVersion ? ChatFormatting.GREEN : ChatFormatting.RED) + Metadata.getVersion(), width - 152, 39, 0xFFFFFF, .75);

        /* Display profile player skin head */
        /*
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.bindTexture(getCachedTexture(Minecraft.getMinecraft().getSession().getPlayerID()).getGlTextureId());
        drawScaledCustomSizeModalRect(width - 153, 12, 0, 0, 30, 30, 23, 25, 30, 30);
        */

        /* Display copyright disclaimers at bottom of screen */
        sfr.drawString(I18n.format("menu.left").toUpperCase(), 1, height - 7, 0x55FFFFFF);
        String s = I18n.format("menu.right").toUpperCase();
        sfr.drawString(s, width - sfr.getWidth(s) - 1, height - 7, 0x55FFFFFF);

        GlStateManager.popMatrix();

    }

    public void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float f1 = (float) this.height * f / 256.0F;
        float f2 = (float) this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    public void rotateAndBlurSkybox(float p_73968_1_) {
        ResourceLocation backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.mc.getTextureManager().bindTexture(backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float) (j - i / 2) / 256.0F;
            worldrenderer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    public void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                if (k == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    public void drawDefaultStyleScreen(int mouseX, int mouseY, float partialTicks) {
        if (Settings.BACKGROUND.equals("DEFAULT")) {
            GlStateManager.disableAlpha();
            this.renderSkybox(mouseX, mouseY, partialTicks);
            GlStateManager.enableAlpha();
        } else {
            // Background
            GlStateManager.disableAlpha();
            this.renderHyperiumBackground(new ScaledResolution(mc));

        }
        ParticleOverlay.getOverlay().render(mouseX, mouseY, 0, 0, 0, 0);

        GlStateManager.enableAlpha();
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(4F, 4F, 1F);
        this.drawCenteredString(fontRendererObj, Metadata.getModid(), width / 8, 40 / 4, 0xFFFFFF);
        GlStateManager.popMatrix();
        String s = String.format("%s %s", Metadata.getModid(), Metadata.getVersion());
        this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);
        String s1 = I18n.format("menu.right");
        this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);
        String s3 = "Made by Sk1er, Kevin,";
        this.drawString(this.fontRendererObj, s3, this.width - this.fontRendererObj.getStringWidth(s3) - 2, this.height - 30, -1);

        String s4 = "Cubxity, CoalOres and boomboompower";
        this.drawString(this.fontRendererObj, s4, this.width - this.fontRendererObj.getStringWidth(s4) - 2, this.height - 20, -1);
        GuiButton hypixelButton = this.hypixelButton;
        if (hypixelButton != null)
            hypixelButton.displayString = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? I18n.format("button.ingame.fixhypixelsession") : I18n.format("button.ingame.joinhypixel");
    }

    public int getIntendedWidth(int value) {
        float intendedWidth = 1920F;
        return (int) ((Minecraft.getMinecraft().displayWidth / intendedWidth) * value);
    }

    public int getIntendedHeight(int value) {
        float intendedHeight = 1080F;
        return (int) ((Minecraft.getMinecraft().displayHeight / intendedHeight) * value);
    }

    @Override
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    @Override
    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(tint);
        }
    }

}
