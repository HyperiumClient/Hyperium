package cc.hyperium.handlers.handlers;

import cc.hyperium.gui.BrowserWindow;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

/**
 * Created by mitchellkatz on 3/10/18. Designed for production use on Sk1er.club
 */
public class BrowserManager {
    private BrowserWindow browser;
    private boolean show = false;
    private boolean maximized = false;

    public void toggleMaximize(){
        if(maximized)
            browser.defaultSize();
        else{
            ScaledResolution current = ResolutionUtil.current();
            browser.setSize(current.getScaledWidth() * current.getScaleFactor() - 20, current.getScaledHeight() * current.getScaleFactor() - 20);
            browser.getMp().setBounds(0, 0, browser.getWidth(), 10);
            browser.getJfx().setBounds(0, 10, browser.getWidth(), browser.getHeight() - 10);
            browser.getMp().getComponent(0).setBounds(browser.getWidth() - 30, 0, 15, 10);
            browser.getMp().getComponent(1).setBounds(browser.getWidth() - 15, 0, 15, 10);
            browser.setLocation(Display.getX() + 10, Display.getY() + 10);
            System.out.println("Resize");
        }
        maximized = !maximized;
    }

    public void browse(String url) {
        show = true;
        if (browser == null)
            browser = new BrowserWindow(url);
        else {
            if (url.equalsIgnoreCase("close"))
                show = false;
            else {
                browser.loadURL(url);
                browser.setVisible(true);
            }
        }
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }
}
