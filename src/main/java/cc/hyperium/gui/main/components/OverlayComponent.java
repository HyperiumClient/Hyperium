package cc.hyperium.gui.main.components;

/*
 * Created by Cubxity on 01/06/2018
 */
public abstract class OverlayComponent {

    public abstract void render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h);

    public abstract void handleMouseInput();
}
