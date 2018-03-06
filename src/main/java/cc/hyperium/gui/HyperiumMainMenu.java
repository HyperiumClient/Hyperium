/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.GuiStyle;
import cc.hyperium.Metadata;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class HyperiumMainMenu extends GuiScreen implements GuiYesNoCallback {


    private static ResourceLocation background = new ResourceLocation("textures/material/backgrounds/1.png");
    private final ResourceLocation exit = new ResourceLocation("textures/material/exit.png");
    private final ResourceLocation people_outline = new ResourceLocation("textures/material/people-outline.png");
    private final ResourceLocation person_outline = new ResourceLocation("textures/material/person-outline.png");
    private final ResourceLocation settings = new ResourceLocation("textures/material/settings.png");
    private final ResourceLocation hIcon = new ResourceLocation("textures/h_icon.png");
    private GuiScreen field_183503_M;
    private boolean field_183502_L;
    private int field_92021_u;
    private int field_92022_t;
    private int field_92024_r;
    private int field_92023_s;
    private DynamicTexture viewportTexture;
    private FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
    private GuiButton hypixelButton;
    private boolean clickedCheckBox = false;
    private HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    private HyperiumFontRenderer sfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    private HashMap<String, DynamicTexture> cachedImages = new HashMap<>();

    public static ResourceLocation getBackground() {
        return background;
    }

    public static void setBackground(ResourceLocation background) {
        HyperiumMainMenu.background = background;
    }

    /**
     * Override initGui
     *
     * @author Cubxity
     */

    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j - 10, 24);


        switch (getStyle()) {
            case DEFAULT:
                addDefaultStyleOptionsButton(j);
                break;
            case HYPERIUM:
                addHyperiumStyleOptionsButton(j);
                break;
        }

        this.mc.setConnectedToRealms(false);

        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.field_183502_L) {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.field_183503_M = realmsbridge.getNotificationScreen(this);
            this.field_183502_L = true;
        }

        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.field_183503_M != null) {
            this.field_183503_M.func_183500_a(this.width, this.height);
            this.field_183503_M.initGui();
        }
    }

    /**
     * Override buttons
     *
     * @author Cubxity
     */

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        switch (getStyle()) {
            case DEFAULT:
                addDefaultStyleSingleplayerMultiplayerButtons(p_73969_1_, p_73969_2_);
                break;
            case HYPERIUM:
                addHyperiumStyleSingleplayerMultiplayerButtons(p_73969_1_, p_73969_2_);
                break;
        }
    }

    /**
     * Override drawScreen method
     *
     * @author Cubxity
     */

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

    @Override
    public void actionPerformed(GuiButton button) {

        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }

        switch (getStyle()) {
            case DEFAULT:
                if (button.id == 15)
                    mc.displayGuiScreen(new ModConfigGui());
                if (button.id == 16)
                    Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "stuck.hypixel.net" : "mc.hypixel.net", 25565));
                break;
            case HYPERIUM:
                if (button.id == 15)
                    mc.displayGuiScreen(new ModConfigGui());
                break;
        }

    }

    private void addHyperiumStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, width / 2 - 295, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(2, width / 2 - 175, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(15, width / 2 + 65, height / 2 - 55, 110, 110, ""));
    }

    private void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        //Change realms button ID to 16 to avoid conflicts
        this.buttonList.add(this.hypixelButton = new GuiButton(16, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("Join Hypixel")));
        this.buttonList.add(new GuiButton(15, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("Hyperium Settings")));
    }

    private void addHyperiumStyleOptionsButton(int j) {
        this.buttonList.add(new GuiButton(0, width / 2 - 55, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(4, width / 2 + 185, height / 2 - 55, 110, 110, ""));
    }

    private void addDefaultStyleOptionsButton(int j) {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
    }


    private void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {
        // Background
        GlStateManager.disableAlpha();
        ScaledResolution sr = new ScaledResolution(mc);
        this.renderHyperiumBackground(sr);

        GlStateManager.enableAlpha();
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);

        // Logo
        ResourceLocation logo = new ResourceLocation("textures/hyperium-logo.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(logo);
        drawScaledCustomSizeModalRect(10, 1, 0, 0, 2160, 500, 180, 35, 2160, 500);

        // Account area
        drawRect(width - 155, 10, width - 10, 40, new Color(0, 0, 0, 60).getRGB());

        // Looks weird with the small green strip
        // drawRect(width - 160, 10, width - 158, 40, new Color(149, 201, 144, 255).getRGB());

        // Reset the color of the renderer
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        fr.drawString(Minecraft.getMinecraft().getSession().getUsername(), width - 123, 19, 0xFFFFFF);

        // Credits
        sfr.drawString("COPYRIGHT 2018 HYPERIUM DEV TEAM", 1, height - 10, 0xFFFFFF);
        String s = "NOT AFFILIATED WITH MOJANG AB";
        sfr.drawString(s, width - sfr.getWidth(s) - 1, height - 10, 0xFFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.bindTexture(getCachedTexture(Minecraft.getMinecraft().getSession().getPlayerID()).getGlTextureId());
        drawScaledCustomSizeModalRect(width - 155, 10, 0, 0, 30, 30, 30, 30, 30, 30);

        // Draw icons on buttons
        TextureManager tm = mc.getTextureManager();

        tm.bindTexture(person_outline);
        drawScaledCustomSizeModalRect(width / 2 - 285, height / 2 - 45, 0, 0, 192, 192, 90, 90, 192, 192);
        tm.bindTexture(people_outline);
        drawScaledCustomSizeModalRect(width / 2 - 165, height / 2 - 45, 0, 0, 192, 192, 90, 90, 192, 192);
        tm.bindTexture(settings);
        drawScaledCustomSizeModalRect(width / 2 - 45, height / 2 - 45, 0, 0, 192, 192, 90, 90, 192, 192);
        tm.bindTexture(hIcon);
        drawScaledCustomSizeModalRect(width / 2 + 85, height / 2 - 35, 0, 0, 104, 104, 70, 70, 104, 104);
        tm.bindTexture(exit);
        drawScaledCustomSizeModalRect(width / 2 + 195, height / 2 - 45, 0, 0, 192, 192, 90, 90, 192, 192);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderHyperiumBackground(ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(background);
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

    private int color(int i, int i1, int i2, int i3) {
        return new Color(i, i1, i2, i3).getRGB();
    }

    private void drawDefaultStyleScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderHyperiumBackground(new ScaledResolution(mc));
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
        String s1 = "Not affiliated with Mojang AB.";
        this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);
        String s3 = "Made by Sk1er, Kevin, Cubxity, CoalOres and boomboompower";
        this.drawString(this.fontRendererObj, s3, this.width - this.fontRendererObj.getStringWidth(s3) - 2, this.height - 20, -1);
        this.hypixelButton.displayString = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "Fix Hypixel Session" : "Join Hypixel";
    }

    private GuiStyle getStyle() {
        return GuiStyle.valueOf(GeneralSetting.menuStyle);
    }

    private DynamicTexture getCachedTexture(String t) {
        final DynamicTexture[] texture = {this.cachedImages.get(t)};
        if (texture[0] == null) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                try {
                    texture[0] = new DynamicTexture(ImageIO
                            .read(new URL("https://crafatar.com/avatars/" + t + "?size=30?default=MHF_Steve")));

                } catch (Exception ignored) {
                    try {
                        texture[0] = new DynamicTexture(ImageIO
                                .read(new URL("https://crafatar.com/avatars/c06f89064c8a49119c29ea1dbd1aab82")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.cachedImages.put(t, texture[0]);
            });

        }
        return texture[0];
    }
}