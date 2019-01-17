package cc.hyperium.mods.browser.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.montoyo.mcef.api.IBrowser;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author Koding
 */
public class GuiConfig extends GuiScreen {

    @ConfigOpt
    public static int x = 10;
    @ConfigOpt
    public static int y = 10;
    @ConfigOpt
    public static int width = 320;
    @ConfigOpt
    public static int height = 180;
    @ConfigOpt
    public static int offsetX = 0;
    @ConfigOpt
    public static int offsetY = 0;
    public static boolean drawSquare = true;
    public IBrowser browser;
    private boolean dragging = false;
    private boolean resizing = false;

    public GuiConfig(IBrowser b) {
        browser = b;
    }

    @Override
    public void handleInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE || Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                drawSquare = false;
                Hyperium.INSTANCE.getModIntegration().getBrowserMod().hudBrowser = this;
                browser.injectMouseMove(-10, -10, 0, true);
                mc.displayGuiScreen(null);
                return;
            }
        }

        while (Mouse.next()) {
            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = mc.displayHeight - Mouse.getEventY();

            if (btn == 1 && pressed && sx >= x && sy >= y && sx < x + width && sy < y + height) {
                browser.injectMouseMove(sx - x, sy - y, 0, false);
                browser.injectMouseButton(sx - x, sy - y, 0, 1, true, 1);
                browser.injectMouseButton(sx - x, sy - y, 0, 1, false, 1);
            } else if (dragging) {
                if (btn == 0 && !pressed)
                    dragging = false;
                else {
                    x = sx + offsetX;
                    y = sy + offsetY;
                }
            } else if (resizing) {
                if (btn == 0 && !pressed) {
                    resizing = false;
                    browser.resize(width, height);
                } else {
                    int w = sx - x;
                    int h = sy - y;

                    if (w >= 32 && h >= 18) {
                        if (h >= w) {
                            double dw = ((double) h) * (16.0 / 9.0);
                            width = (int) dw;
                            height = h;
                        } else {
                            double dh = ((double) w) * (9.0 / 16.0);
                            width = w;
                            height = (int) dh;
                        }
                    }
                }
            } else if (pressed && btn == 0 && sx >= x && sy >= y && sx < x + width && sy < y + height) { //In browser rect
                dragging = true;
                offsetX = x - sx;
                offsetY = y - sy;
            } else if (pressed && btn == 0 && sx >= x + width && sy >= y + height && sx < x + width + 10 && sy < y + height + 10) //In resize rect
                resizing = true;
        }
    }

    public void drawScreen(final int i1, final int i2, final float f) {
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        this.browser.draw(this.unscaleX(x), this.unscaleY(height + y), this.unscaleX(width + x), this.unscaleY(y));
        if (drawSquare) {
            final Tessellator t = Tessellator.getInstance();
            final WorldRenderer wr = t.getWorldRenderer();
            wr.begin(2, DefaultVertexFormats.POSITION_COLOR);
            wr.pos(this.unscaleX(x + width), this.unscaleY(y + height), 0.0).color(255, 255, 255, 255).endVertex();
            wr.pos(this.unscaleX(x + width + 10), this.unscaleY(y + height), 0.0).color(255, 255, 255, 255).endVertex();
            wr.pos(this.unscaleX(x + width + 10), this.unscaleY(y + height + 10), 0.0).color(255, 255, 255, 255).endVertex();
            wr.pos(this.unscaleX(x + width), this.unscaleY(y + height + 10), 0.0).color(255, 255, 255, 255).endVertex();
            t.draw();
        }
        GL11.glEnable(2929);
    }


    public double unscaleX(int x) {
        return ((double) x) / ((double) mc.displayWidth) * ((double) super.width);
    }

    public double unscaleY(int y) {
        return ((double) y) / ((double) mc.displayHeight) * ((double) super.height);
    }

}
