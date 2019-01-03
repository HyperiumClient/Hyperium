package cc.hyperium.gui.main.components;

import cc.hyperium.gui.ColourOptions;
import cc.hyperium.utils.GraphicsUtil;
import cc.hyperium.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.function.Consumer;

/*
 * Created by Cubxity on 01/06/2018
 */
public class OverlayToggle extends OverlayComponent {
    private boolean toggle;
    private Consumer<Boolean> callback;
    private float step = 0f;
    private float colorStep = 0.5f;

    public OverlayToggle(String label, boolean toggle, Consumer<Boolean> callback, boolean enabled) {
        super(enabled);
        this.label = label;
        this.toggle = toggle;
        this.callback = callback;

        if (enabled) {
            this.toggle = toggle;
        } else {
            this.toggle = false;
        }
    }

    @Override
    public boolean render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h, int overlayH) {
        if (!super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH))
            return false;
        // Render toggle button.
        if (enabled) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int color = GraphicsUtil.INSTANCE.transitionOfHueRange(colorStep, 1, getHue(ColourOptions.accent_r, ColourOptions.accent_g, ColourOptions.accent_b), 0.7F, 0.7F).getRGB();
            //int color = GraphicsUtil.INSTANCE.transitionOfHueRange(colorStep, 1, 88, 0.7F, 0.7F).getRGB();
            RenderUtils
                .drawSmoothRect(overlayX + w - 30, overlayY + 5, overlayX + w - 5, overlayY + h - 5,
                    color);
            RenderUtils.drawFilledCircle(toggle ? (int) (overlayX + w - 10 - (step * 15))
                    : (int) (overlayX + w - 25 + (step * 15)), overlayY + h / 2, 4,
                new Color(30, 30, 30).getRGB());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        } else {
            RenderUtils
                .drawSmoothRect(overlayX + w - 30, overlayY + 5, overlayX + w - 5, overlayY + h - 5,
                    new Color(169, 169, 169).getRGB());
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
        if (!enabled) {
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

    /**
     * @author ConorTheDev
     */
    private int getHue(int red, int green, int blue) {

        float min = Math.min(Math.min(red, green), blue);
        float max = Math.max(Math.max(red, green), blue);

        if (min == max) {
            return 0;
        }

        float hue = 0f;
        if (max == red) {
            hue = (green - blue) / (max - min);

        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);

        } else {
            hue = 4f + (red - green) / (max - min);
        }

        hue = hue * 60;
        if (hue < 0) hue = hue + 360;

        return Math.round(hue);
    }
}
