package cc.hyperium.gui.main.components;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.OldHyperiumMainGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.Color;

/*
 * Created by Cubxity on 29/05/2018
 */
public class SettingItem {
    public int clickX = -1, clickY = -1;
    private Runnable onClick;
    private ResourceLocation icon;
    private String title;
    private String desc;
    private String hover;
    private int xIndex, yIndex;
    private boolean lastClicked = false;

    public SettingItem(Runnable onClick, ResourceLocation icon, String title, String desc, String hover, int xIndex, int yIndex) {
        this.onClick = onClick;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.hover = hover;
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public void handleMouseInput(int mouseX, int mouseY, int containerWidth, int containerHeight, int topX, int topY) {
/*
        System.out.println("In mouseInput: " + mouseX + " " + mouseY);
        if (Mouse.isButtonDown(0)) {
            int w = containerWidth / 4;
            int h = containerHeight / 4;
            int blockX = topX + w * xIndex + w / 7;
            int blockY = topY + h * yIndex + h / 6;
            if (mouseX >= blockX && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY && mouseY <= blockY + h / 6 * 4)
                onClick.run();
        }
*/
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if (OldHyperiumMainGui.INSTANCE.getOverlay() != null) return;
        if (System.currentTimeMillis() - OldHyperiumMainGui.INSTANCE.getLastSelectionChange() < 100) return;
        this.clickX = mouseX;
        this.clickY = mouseY;
    }

    public void render(int mouseX, int mouseY, int containerWidth, int containerHeight, int topX, int topY) {
        int w = containerWidth / 3;
        int h = containerHeight / 3;
        int blockX = topX + w * xIndex + w / 7;
        int blockY = topY + h * yIndex + h / 6;
        int bottom = blockY + h / 6 * 4;
        if (mouseX >= blockX && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY && mouseY <= bottom && OldHyperiumMainGui.INSTANCE.getOverlay() == null) {
            HyperiumGui.drawChromaBox(blockX, blockY, blockX + w / 7 * 5, bottom, 0.2f);
        }
        else {
            Gui.drawRect(blockX, blockY, blockX + w / 7 * 5, bottom, new Color(0, 0, 0, 60).getRGB());
            GlStateManager.shadeModel(7424); // for opening from main menu
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
        }
        if (clickX >= blockX && clickX <= blockX + w / 7 * 5 && clickY >= blockY && clickY <= bottom) {
            //Added by Sk1er to prevent switching to a different item while in another
            if (OldHyperiumMainGui.INSTANCE.getOverlay() == null) {
                onClick.run();
            }
        }
        lastClicked = Mouse.isButtonDown(0);
        OldHyperiumMainGui.INSTANCE.getFr().drawString(title, blockX + 3, blockY + 3, 0xffffff);
        float s = 0.8f;
        if (desc != null) {
            GlStateManager.scale(s, s, s);
            StringBuilder tmp = new StringBuilder();
            int offsetY = 15;
            for (String word : desc.split(" ")) {
                if (word.equalsIgnoreCase("\n")) {
                    OldHyperiumMainGui.INSTANCE.getFr().drawString(tmp.toString(), (blockX + 3) / s, (blockY + offsetY) / s, new Color(160, 160, 160).getRGB());
                    offsetY += 10;
                    tmp = new StringBuilder();
                    float v = (blockY + offsetY + 5) / s;
                    float v1 = bottom / s;
                    if (v > v1) {
                        tmp = new StringBuilder();
                        break;
                    }
                    continue;
                }
                if (OldHyperiumMainGui.INSTANCE.getFr().getWidth(tmp + word) * s < w / 7 * 5 - (offsetY >= 25 ? 0 : 27))
                    tmp.append(word).append(" ");
                else {
                    OldHyperiumMainGui.INSTANCE.getFr().drawString(tmp.toString(), (blockX + 3) / s, (blockY + offsetY) / s, new Color(160, 160, 160).getRGB());

                    /* Cuts off rendering if the next line will be below the border
                     *
                     * Triggers if:
                     * borderY + lineY divided by the scale is greater than the borders Y height + box height.
                     * Taken from the rendering code above. This should be changed in the future.
                     *
                     * By boom
                     */
                    offsetY += 10;
                    float v = (blockY + offsetY + 5) / s;
                    float v1 = bottom / s;
                    if (v > v1) {
                        tmp = new StringBuilder();
                        break;
                    }


                    tmp = new StringBuilder();
                    tmp.append(word).append(" ");
                }
            }
            if (tmp.length() > 0)
                OldHyperiumMainGui.INSTANCE.getFr().drawString(tmp.toString(), (blockX + 3) / s, (blockY + offsetY) / s, new Color(160, 160, 160).getRGB());
            GlStateManager.scale(1.25f, 1.25f, 1.25f);
        }

        Icons.SETTINGS.bind();

        if (icon != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
            int oldScale = Minecraft.getMinecraft().gameSettings.guiScale;
            float v = 2F / oldScale;
            int width = (int) (25 * v);
            Gui.drawScaledCustomSizeModalRect((blockX + w / 7 * 5 - 27) + (25 - width), (blockY - 14) + width, 0, 0, 144, 144, width, width, 144, 144);
        }

        Icons.INFO.bind();
        Gui.drawScaledCustomSizeModalRect(blockX + w / 7 * 5 - 10, bottom - 10, 0, 0, 144, 144, 10, 10, 144, 144);
        if (mouseX >= blockX + w / 7 * 5 - 10 && mouseX <= blockX + w / 7 * 5 && mouseY >= blockY + h / 7 * 5 - 10 && mouseY < blockY + h / 7 * 5 && OldHyperiumMainGui.INSTANCE.getOverlay() == null) {
            HyperiumGui.drawChromaBox(mouseX + 10, mouseY, (int) (mouseX + 10 + OldHyperiumMainGui.INSTANCE.getFr().getWidth(hover) * s + 3), (int) (mouseY + OldHyperiumMainGui.INSTANCE.getFr().FONT_HEIGHT * s + 3), 0.2f);
            GlStateManager.scale(s, s, s);
            OldHyperiumMainGui.INSTANCE.getFr().drawString(hover, (mouseX + 10) / s + 1, mouseY / s + 1, 0xffffff);
            GlStateManager.scale(1.25f, 1.25f, 1.25f);
        }
        this.clickY = -1;
        this.clickX = -1;

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getClickX() {
        return clickX;
    }

    public void setClickX(int clickX) {
        this.clickX = clickX;
    }

    public int getClickY() {
        return clickY;
    }

    public void setClickY(int clickY) {
        this.clickY = clickY;
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHover() {
        return hover;
    }

    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public boolean isLastClicked() {
        return lastClicked;
    }

    public void setLastClicked(boolean lastClicked) {
        this.lastClicked = lastClicked;
    }


}
