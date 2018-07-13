package cc.hyperium.gui.main.components;

import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.utils.GraphicsUtil;
import cc.hyperium.utils.RenderUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

/*
 * Created by Cubxity on 01/06/2018
 */
public class OverlayToggle extends OverlayComponent {
    private boolean toggle;
    private Consumer<Boolean> callback;
    private float step = 0f;
    private float colorStep = 0.5f;
    private boolean enabled = true;

    public OverlayToggle(String label, boolean toggle, Consumer<Boolean> callback, boolean enabled) {
        this.label = label;
        this.toggle = toggle;
        this.callback = callback;
        this.enabled = enabled;

        if(enabled){
            this.toggle = toggle;
        } else{
            this.toggle = false;
        }
    }

    @Override
    public boolean render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h, int overlayH) {
        // Render name of setting as text.
        int textY = (overlayY + (h - HyperiumMainGui.INSTANCE.getFr().FONT_HEIGHT) / 2);
        if (textY < (overlayH / 4)) {
            return false;
        } else if ((textY + h) > (overlayH / 4 * 3)) {
            return false;
        }
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, 0x1e000000);
        }
        if(enabled) {
            HyperiumMainGui.INSTANCE.getFr().drawString(label, overlayX + 4,
                (overlayY + (h - HyperiumMainGui.INSTANCE.getFr().FONT_HEIGHT) / 2), 0xffffff);
        } else {
            HyperiumMainGui.INSTANCE.getFr().drawString(label, overlayX + 4,
                (overlayY + (h - HyperiumMainGui.INSTANCE.getFr().FONT_HEIGHT) / 2), 0xA9A9A9);
        }

        // Render toggle button.
        if(enabled) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int color = GraphicsUtil.INSTANCE.transitionOfHueRange(colorStep, 1, 88, 0.7F, 0.7F).getRGB();
            RenderUtils
                .drawSmoothRect(overlayX + w - 30, overlayY + 5, overlayX + w - 5, overlayY + h - 5,
                    color);
            RenderUtils.drawFilledCircle(toggle ? (int) (overlayX + w - 10 - (step * 15))
                    : (int) (overlayX + w - 25 + (step * 15)), overlayY + h / 2, 4,
                new Color(30, 30, 30).getRGB());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        } else{
            RenderUtils
                .drawSmoothRect(overlayX + w - 30, overlayY + 5, overlayX + w - 5, overlayY + h - 5,
                    new Color(169,169,169).getRGB());
            RenderUtils.drawFilledCircle(toggle ? (int) (overlayX + w - 10 - (step * 15))
                    : (int) (overlayX + w - 25 + (step * 15)), overlayY + h / 2, 4,
                new Color(30, 30, 30).getRGB());
        }

        if (step > 0f)
            step -= 0.1f;
        if (toggle && colorStep < 1f)
            colorStep += 0.2f;
        if (!toggle && colorStep > 0f)
            colorStep -= 0.2f;
        return true;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {
        if(!enabled){
            return;
        }
        if (mouseX >= overlayX + w - 30 && mouseX <= overlayX + w - 5 && mouseY >= overlayY + 5 && mouseY <= overlayY + h - 5) {
            toggle = !toggle;
            if (callback != null)
                callback.accept(toggle);
            step = 1f;
        }
    }

    public void resetStep() {
        step = 0f;
        colorStep = 0.5f;
    }
}
