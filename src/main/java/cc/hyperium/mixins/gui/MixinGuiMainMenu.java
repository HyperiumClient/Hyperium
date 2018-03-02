/*
 * Hyperium Client, Free client with huds and popular mod
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

package cc.hyperium.mixins.gui;

import cc.hyperium.GuiStyle;
import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.ModConfigGui;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    boolean overLast = false;
    @Shadow
    private ResourceLocation backgroundTexture;
    @Shadow
    private String openGLWarning2;
    @Shadow
    private String openGLWarning1;
    @Shadow
    private GuiScreen field_183503_M;
    @Shadow
    private boolean field_183502_L;
    @Shadow
    private int field_92019_w;
    @Shadow
    private int field_92020_v;
    @Shadow
    @Final
    private Object threadLock;
    @Shadow
    private int field_92021_u;
    @Shadow
    private int field_92022_t;
    @Shadow
    private int field_92024_r;
    @Shadow
    private int field_92023_s;
    @Shadow
    private DynamicTexture viewportTexture;
    private FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
    private GuiButton hypixelButton;
    private boolean clickedCheckBox = false;
    private HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    private HyperiumFontRenderer sfr = new HyperiumFontRenderer("Arial", Font.PLAIN, 12);
    private HashMap<String, DynamicTexture> cachedImages = new HashMap<>();

    /**
     * Override initGui
     *
     * @author Cubxity
     */
    @Overwrite
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        int j = this.height / 4 + 48;

        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(j - 10, 24);
        }

        switch (getStyle()){
            case DEFAULT:
                addDefaultStyleOptionsButton(j);
                synchronized (this.threadLock) {
                    this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
                    this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
                    int k = Math.max(this.field_92023_s, this.field_92024_r);
                    this.field_92022_t = (this.width - k) / 2;
                    if (this.buttonList.size() > 0) {
                        this.field_92021_u = this.buttonList.get(0).yPosition - 24;
                    }
                    this.field_92020_v = this.field_92022_t + k;
                    this.field_92019_w = this.field_92021_u + 24;
                }
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

        if (this.func_183501_a()) {
            this.field_183503_M.func_183500_a(this.width, this.height);
            this.field_183503_M.initGui();
        }
    }

    /**
     * Override buttons
     *
     * @author Cubxity
     */
    @Overwrite
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        switch (getStyle()){
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
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        switch (getStyle()){
            case DEFAULT:
                drawDefaultStyleScreen(mouseX, mouseY, partialTicks);
                break;
            case HYPERIUM:
                drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
                break;
        }
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "By continuing, you acknowledge this client is " + ChatColor.BOLD + "USE AT YOUR OWN RISK", width / 2, 90, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "The developers of Hyperium are not responsible for any damages or bans ", width / 2, 100, Color.WHITE.getRGB());
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "to your account while using this client", width / 2, 110, Color.WHITE.getRGB());

            //
            drawCenteredString(this.fontRendererObj, ChatColor.RED + "Please check the box to confirm you agree with these terms", width / 2, 120, Color.WHITE.getRGB());

            GuiBlock block = new GuiBlock(width / 2 - 10, width / 2 + 10, 135, 155);
            if (!overLast && Mouse.isButtonDown(0) && block.isMouseOver(mouseX, mouseY)) {
                clickedCheckBox = !clickedCheckBox;
            }

            RenderUtils.drawBorderedRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 7, Color.BLACK.getRGB(), Color.RED.getRGB());
            if (clickedCheckBox) {
                RenderUtils.drawLine(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 5, Color.BLACK.getRGB());
                RenderUtils.drawLine(block.getLeft(), block.getBottom(), block.getRight(), block.getTop(), 5, Color.BLACK.getRGB());
                int hoverColor = new Color(0, 0, 0, 60).getRGB();
                int color = new Color(0, 0, 0, 50).getRGB();
                GuiBlock block1 = new GuiBlock(width / 2 - 100, width / 2 + 100, 170, 190);
                Gui.drawRect(block1.getLeft(), block1.getTop(), block1.getRight(), block1.getBottom(), block1.isMouseOver(mouseX, mouseY) ? hoverColor : color);

                if (block1.isMouseOver(mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    Hyperium.INSTANCE.acceptTos();
                }
                drawCenteredString(fontRendererObj, ChatColor.RED + "Accept", width / 2, 175, Color.WHITE.getRGB());
            }
            overLast = Mouse.isButtonDown(0);
        } else
            super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            ci.cancel();
            return;
        }
        switch (getStyle()){
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

    private void addHyperiumStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_){
        this.buttonList.add(new GuiButton(1, width / 2 - 295, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(2, width / 2 - 175, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(15, width / 2 + 65, height / 2 - 55, 110, 110, ""));
    }

    private void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_){
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        //Change realms button ID to 16 to avoid conflicts
        this.buttonList.add(this.hypixelButton = new GuiButton(16, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("Join Hypixel")));
        this.buttonList.add(new GuiButton(15, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 3, I18n.format("Hyperium Settings")));
    }

    private void addHyperiumStyleOptionsButton(int j){
        this.buttonList.add(new GuiButton(0, width / 2 - 55, height / 2 - 55, 110, 110, ""));
        this.buttonList.add(new GuiButton(4, width / 2 + 185, height / 2 - 55, 110, 110, ""));
    }

    private void addDefaultStyleOptionsButton(int j){
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
    }


    private void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks){
        // Background
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);

        // Logo
        ResourceLocation logo = new ResourceLocation("textures/hyperium-logo.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(logo);
        drawScaledCustomSizeModalRect(10, 1, 0, 0, 2160, 500, 180, 35, 2160, 500);

        // Account area
        drawRect(width - 155, 10, width - 10, 40,  new Color(0, 0, 0, 60).getRGB());
        
        // Looks weird with the small green strip
        // drawRect(width - 160, 10, width - 158, 40, new Color(149, 201, 144, 255).getRGB());

        // Reset the color of the renderer
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Bind
        GlStateManager.bindTexture(getCachedTexture(Minecraft.getMinecraft().getSession().getPlayerID()).getGlTextureId());
        drawScaledCustomSizeModalRect(width - 155, 10, 0, 0, 30, 30, 30, 30, 30, 30);
        fr.drawString(Minecraft.getMinecraft().getSession().getUsername(), width - 123, 19, 0xFFFFFF);

        // Credits
        sfr.drawString("COPYRIGHT 2018 HYPERIUM DEV TEAM", 0, height - 10, 0xFFFFFF);
        String s = "NOT AFFILIATED WITH MOJANG AB";
        sfr.drawString(s, width - sfr.getWidth(s), height - 10, 0xFFFFFF);
    }

    private int color(int i, int i1, int i2, int i3) {
        return new Color(i, i1, i2, i3).getRGB();
    }

    private void drawDefaultStyleScreen(int mouseX, int mouseY, float partialTicks){
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
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

    @Shadow
    protected abstract void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_);

    @Shadow
    protected abstract void addDemoButtons(int p_73972_1_, int p_73972_2_);

    @Shadow
    protected abstract boolean func_183501_a();

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