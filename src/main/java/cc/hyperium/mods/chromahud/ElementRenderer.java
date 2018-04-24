/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud;

import cc.hyperium.Hyperium;
import cc.hyperium.event.*;
import cc.hyperium.utils.RenderUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class ElementRenderer {

    protected static ScaledResolution resolution;
    private static double currentScale = 1.0;
    private static int color;
    private static int[] COLORS = new int[]{16777215, 16711680, 65280, 255, 16776960, 11141290};
    private static boolean display = false;
    private static List<Long> clicks = new ArrayList<>();
    private static DisplayElement current;
    private static FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
    private static String cValue;
    boolean last = false;
    private ChromaHUD mod;
    private Minecraft minecraft;

    public ElementRenderer(ChromaHUD mod) {
        this.mod = mod;
        minecraft = Minecraft.getMinecraft();
    }

    public static String getCValue() {
        return cValue;
    }

    public static double getCurrentScale() {
        return currentScale;
    }

    public static int getColor(int c, int x) {
        return c;

    }

    public static void display() {
        display = true;
    }

    public static void draw(int x, double y, String string) {
        List<String> tmp = new ArrayList<>();
        tmp.add(string);
        draw(x, y, tmp);
    }

    public static int RGBtoHEX(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            if (hex.length() == 5)
                hex = "0" + hex;
            if (hex.length() == 4)
                hex = "00" + hex;
            if (hex.length() == 3)
                hex = "000" + hex;
        }
        hex = "#" + hex;
        return Integer.decode(hex);
    }

    public static void draw(int x, double y, List<String> list) {
        int tx = x;
        double ty = y;
        for (String string : list) {
            int shift = current.isRightSided()
                    ? fontRendererObj.getStringWidth(string)
                    : 0;
            if (current.isHighlighted()) {
                if(current.getDisplayItems().stream().anyMatch(i -> i.getType().contains("CB"))){
                    RenderUtils.drawRect((int) ((tx - 1) / getCurrentScale() - shift), (int) ((ty - 3) / getCurrentScale()), (int) ((tx + 1) / getCurrentScale()) + 60 - shift, (int) ((ty + 3) / getCurrentScale()) + 8, new Color(0, 0, 0, 120).getRGB());
                } else {
                    int stringWidth = fontRendererObj.getStringWidth(string);
//                Gui.drawRect((int) ((tx - 1) / getCurrentScale() - shift), (int) ((ty - 1) / getCurrentScale()), (int) ((tx + 1) / getCurrentScale()) + stringWidth - shift, (int) ((ty + 1) / getCurrentScale()) + 8, new Color(0, 0, 0, 120).getRGB());
                    RenderUtils.drawRect((int) ((tx - 1) / getCurrentScale() - shift), (int) ((ty - 1) / getCurrentScale()), (int) ((tx + 1) / getCurrentScale()) + stringWidth - shift, (int) ((ty + 1) / getCurrentScale()) + 8, new Color(0, 0, 0, 120).getRGB());
                }
            }
            if (current.isChroma()) {
                if(current.getDisplayItems().stream().anyMatch(i -> i.getType().contains("CB")))
                    drawChromaString(string, ((60 - fontRendererObj.getStringWidth(string)) / 2) + (tx - shift), (int) ty);
                else
                    drawChromaString(string, tx - shift, (int) ty);
            } else {
                if(current.getDisplayItems().stream().anyMatch(i -> i.getType().contains("CB")))
                    fontRendererObj.drawString(string, (float) (((60 - fontRendererObj.getStringWidth(string)) / 2) + (tx / getCurrentScale() - shift)), (int) (ty / getCurrentScale()), getColor(color, x), current.isShadow());
                else
                    fontRendererObj.drawString(string, (int) (tx / getCurrentScale() - shift), (int) (ty / getCurrentScale()), getColor(color, x), current.isShadow());
            }
            ty += 10;
        }
    }


    //Don't shift, by the time it is here it is already shifted
    public static void drawChromaString(String text, int xIn, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn;
        for (char c : text.toCharArray()) {
            long dif = (x * 10) - (y * 10);
            if (current.isStaticChroma())
                dif = 0;
            long l = System.currentTimeMillis() - dif;
            float ff = current.isStaticChroma() ? 1000.0F : 2000.0F;
            int i = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float) ((double) x / getCurrentScale()), (float) ((double) y / getCurrentScale()), i, current.isShadow());
            x += (double) renderer.getCharWidth(c) * getCurrentScale();
        }
    }


    private static boolean isChromaInt(int e) {
        return e >= 0 && e <= 1;
    }

    public static int maxWidth(List<String> list) {
        int max = 0;
        for (String s : list) {
            max = Math.max(max, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
        }
        return max;
    }

    public static int getColor() {
        return color;
    }

    public static int getCPS() {
        Iterator<Long> iterator = clicks.iterator();
        while (iterator.hasNext())
            if (System.currentTimeMillis() - iterator.next() > 1000L)
                iterator.remove();
        return clicks.size();
    }

    public static DisplayElement getCurrent() {
        return current;
    }

    public static void render(List<ItemStack> itemStacks, int x, double y, boolean showDurability) {
        int line = 0;
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for (ItemStack stack : itemStacks) {
            String dur = (stack.getMaxDamage() - stack.getItemDamage()) + "/" + stack.getMaxDamage();
            renderItem.renderItemAndEffectIntoGUI(stack, (int) (x / ElementRenderer.getCurrentScale() - (current.isRightSided() ? (showDurability ? 16 + (double) 20 * currentScale + fontRendererObj.getStringWidth(dur) : -16) : 0)), (int) ((y + (16 * line * ElementRenderer.getCurrentScale())) / ElementRenderer.getCurrentScale()));
            if (showDurability) {
                ElementRenderer.draw((int) (x + (double) 20 * currentScale), y + (16 * line) + 8, dur);
            }
            line++;
        }
    }

    public static void startDrawing(DisplayElement element) {
        GlStateManager.scale(element.getScale(), element.getScale(), 0);
        currentScale = element.getScale();
        color = element.getColor();
        current = element;
    }

    public static void endDrawing(DisplayElement element) {
        GlStateManager.scale(1.0 / element.getScale(), 1.0 / element.getScale(), 0);
    }


    public static FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    @InvokeEvent
    public void tick(TickEvent event) {

        if (display) {
            Minecraft.getMinecraft().displayGuiScreen(mod.getConfigGuiInstance());
            display = false;
            int j = 0;
        }
        if (Minecraft.getMinecraft().inGameHasFocus)
            cValue = Minecraft.getMinecraft().renderGlobal.getDebugInfoRenders().split("/")[0].trim();
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        resolution = new ScaledResolution(Minecraft.getMinecraft());
        if (!this.minecraft.inGameHasFocus || this.minecraft.gameSettings.showDebugInfo) {
            return;
        }
//        if (!MiscUtil.shouldRenderHUD())
//            return;
//        GlStateManager.color(1.0F,1.0F,1.0F,1.0F);

        renderElements();

//        GlStateManager.color(1.0F,1.0F,1.0F,1.0F);

    }

    public void renderElements() {
        //TODO add CONFIG option to show items when not on Hypixel
        String setting = "chromaHudNonHypixelEnabled";
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get("cc.hyperium.gui.settings.items.GeneralSetting").getAsJsonObject();
        boolean multiServerEnabled;
        if (!generalJsonObject.has(setting)) {
            generalJsonObject.addProperty(setting, true);
            Hyperium.CONFIG.save();
        }
        multiServerEnabled = generalJsonObject.get(setting).getAsBoolean();
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() && !multiServerEnabled)
          return;

        if (fontRendererObj == null)
            fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        boolean m = Mouse.isButtonDown(0);
        if (m != last) {
            last = m;
            if (m) {
                clicks.add(System.currentTimeMillis());
            }
        }

        GlStateManager.color(1.0F,1.0F,1.0F,1.0F);

        List<DisplayElement> elementList = mod.getDisplayElements();
        for (DisplayElement element : elementList) {
            startDrawing(element);
            try {
                element.draw();
            } catch (Exception ignored) {
                // lmao this is so unsafe sk1er XDDDDDDDDD
                //Dude it's in a try catch so that it IS safe!
            }
            endDrawing(element);
        }

    }
}
