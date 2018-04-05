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

package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.BrowserWindow;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

/**
 * @author Sk1er
 */
public class BrowserManager {
    
    private BrowserWindow browser;
    private boolean show = false;
    private boolean maximized = false;
    
    public void toggleMaximize() {
        if (this.maximized) {
            this.browser.setToDefaultSize();
        } else {
            ScaledResolution current = ResolutionUtil.current();
            this.browser.setSize(current.getScaledWidth() * current.getScaleFactor() - 20,
                current.getScaledHeight() * current.getScaleFactor() - 20);
            this.browser.getMotionPanel().setBounds(0, 0, browser.getWidth(), 10);
            this.browser.getBrowserView().setBounds(0, 10, browser.getWidth(), browser.getHeight() - 10);
            this.browser.getMotionPanel().getComponent(0).setBounds(browser.getWidth() - 30, 0, 15, 10);
            this.browser.getMotionPanel().getComponent(1).setBounds(browser.getWidth() - 15, 0, 15, 10);
            this.browser.setLocation(Display.getX() + 10, Display.getY() + 10);
            this.browser.getBrowser().setZoomLevel(0.0);
            System.out.println("Resize");
        }
        this.maximized = !this.maximized;
    }
    
    public void browse(String url) {
        this.show = true;
        if (this.browser == null) {
            this.browser = new BrowserWindow(url);
        } else {
            if (url.equalsIgnoreCase("close")) {
                this.show = false;
            } else {
                this.browser.loadURL(url);
                this.browser.setVisible(true);
            }
        }
        this.browser.getBrowser().setZoomLevel(-3.8017840169239308);
    }
    
    @InvokeEvent
    public void onTick(TickEvent e){
        if(browser == null)return;
        if(!Display.isActive() && !browser.isFocused() && browser.isVisible()) {
            browser.setVisible(false);
        }

        if (browser.isVisible() && !show) browser.setVisible(false);

        else if(Display.isActive() && !browser.isVisible() && show)
            browser.setVisible(true);
    }
    
    public void setShow(boolean show) {
        this.show = show;
    }
    
    public boolean isShow() {
        return this.show;
    }
}
