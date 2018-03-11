package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.BrowserWindow;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

/**
 * Created by mitchellkatz on 3/10/18. Designed for production use on Sk1er.club
 */
public class BrowserManager {
    private BrowserWindow browser;
    private boolean show;
    private boolean hadFocus;

    @InvokeEvent
    public void tick(TickEvent ev) {
        if (browser != null) {
            boolean b = (Display.isActive() || browser.getJfx().hasFocus()) && show;
            if (browser.isVisible() != b) {
                browser.setVisible(b);
            }

            if (browser.getJfx().hasFocus()) {
                if (!hadFocus) {
                    ScaledResolution current = ResolutionUtil.current();
                    browser.setSize(current.getScaledWidth() * current.getScaleFactor() - 20, current.getScaledHeight() * current.getScaleFactor() - 20);
                    browser.getMp().setBounds(0, 0, browser.getWidth(), 10);
                    browser.getJfx().setBounds(0, 10, browser.getWidth(), browser.getHeight() - 10);
                    browser.setLocation(Display.getX() + 10, Display.getY() + 10);
                    System.out.println("Resize");
                }
            } else {
                if (hadFocus)
                    browser.defaultSize();

            }
            hadFocus = browser.getJfx().hasFocus();
        }

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
}
