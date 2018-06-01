package cc.hyperium.gui.main.components;

import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.util.function.Consumer;

/*
 * Created by Cubxity on 01/06/2018
 */
public class OverlayToggle extends OverlayComponent {
    private boolean toggle;
    private Consumer<Boolean> callback;

    public OverlayToggle(String label, boolean toggle, Consumer<Boolean> callback) {
        this.label = label;
        this.toggle = toggle;
        this.callback = callback;
    }

    @Override
    public void render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {
        GlStateManager.disableBlend();
        RenderUtils.drawSmoothRect(overlayX + w - 30, overlayY + 2, overlayX + w - 5, overlayY + w - 2, 0xffffff);
        GlStateManager.enableBlend();
        super.render(mouseX, mouseY, overlayX, overlayY, w, h);
    }

    @Override
    public void handleMouseInput(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {

    }
}
