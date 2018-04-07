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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.installer.components.MotionPanel;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.Display;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A window which can be used to quickly view a webpage in-game
 */
public class BrowserWindow extends JFrame {
    
    public static final int WIDTH = 400;
    public static final int HEIGHT = 250;
    private float scale = 1.0F;
    private Browser browser;
    private BrowserView browserView;
    private MotionPanel mp;
    private boolean ctrlPressed = false;
    private boolean shiftPressed = false;
    
    public BrowserWindow(String url) {
        CookieHandler cookieManager;
        
        cookieManager = new CookieManager();
        
        CookieHandler.setDefault(cookieManager);
        
        super.frameInit();
        initComponents();
        setToDefaultSize();
        loadURL(url);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                BrowserWindow.this.browser.stop();
            }
        });
        setTitle("Browser");
        this.setVisible(true);
        this.setLayout(null);
        setResizable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    BrowserWindow.this.ctrlPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    BrowserWindow.this.shiftPressed = false;
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = true;
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                } else if (ctrlPressed && e.getKeyCode() == KeyEvent.VK_PLUS
                    || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                    browser.zoomIn();
                } else if (ctrlPressed && e.getKeyCode() == KeyEvent.VK_MINUS
                    || e.getKeyCode() == KeyEvent.VK_UNDERSCORE) {
                    browser.zoomOut();
                } else if (ctrlPressed && shiftPressed && e.getKeyCode() == KeyEvent.VK_PLUS
                    || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                    scale += 0.1;
                    scale(scale);
                } else if (ctrlPressed && shiftPressed && e.getKeyCode() == KeyEvent.VK_MINUS
                    || e.getKeyCode() == KeyEvent.VK_UNDERSCORE) {
                    scale -= 0.1;
                    scale(scale);
                }
            }
        });
    }
    
    /**
     * Gets the url version of the string, returns null if the url is not valid
     *
     * @param str the url to parse
     * @return the parsed url or null if invalid
     */
    private String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }
    
    /**
     * Loads a url into the browser
     *
     * @param url the url to load
     */
    public void loadURL(final String url) {
        String tmp = toURL(url);
        if (tmp == null) {
            tmp = toURL("https://" + url);
        }
        
        // Final check
        if (tmp != null) {
            this.browser.loadURL(tmp);
        }
    }
    
    public Browser getBrowser() {
        return this.browser;
    }
    
    public BrowserView getBrowserView() {
        return this.browserView;
    }
    
    public MotionPanel getMotionPanel() {
        return this.mp;
    }
    
    /**
     * Sets the browser window to its default size
     */
    public void setToDefaultSize() {
        // Cross call
        scale(1);
    }
    
    /**
     * Scales the browser
     *
     * @param scale the scale of the browser
     */
    private void scale(float scale) {
        int width = (int) Math.max(WIDTH * scale, ResolutionUtil.current().getScaledWidth() / 8);
        int height = (int) Math.max(HEIGHT * scale, ResolutionUtil.current().getScaledHeight() / 8);
        setSize(width, height);
        ScaledResolution current = ResolutionUtil.current();
        int rightX = Display.getX() + current.getScaledWidth() * current.getScaleFactor();
        int bottomY = Display.getY() + current.getScaledHeight() * current.getScaleFactor();
        
        this.setLocation(rightX - width, bottomY - height);
        mp.setBounds(0, 0, getWidth(), 10);
        mp.getComponent(0).setBounds(getWidth() - 30, 0, 15, 10);
        mp.getComponent(1).setBounds(getWidth() - 15, 0, 15, 10);
        browserView.setBounds(0, 10, getWidth(), getHeight() - 10);
        browser.setZoomLevel(-3.8017840169239308);
    }
    
    private void initComponents() {
        Container container = getContentPane();
        container.setLayout(null);
        browser = new Browser();
        browserView = new BrowserView(browser);
        
        mp = new MotionPanel(this);
        mp.setBounds(0, 0, getWidth(), 10);
        mp.setBackground(new Color(30, 30, 30));
        mp.setLayout(null);
        
        JButton max = new JButton();
        max.setBackground(new Color(40, 40, 40));
        max.setSize(15, 10);
        max.setBounds(getWidth() - 30, 0, 15, 10);
        max.setBorderPainted(false);
        max.setFocusPainted(false);
        max.addActionListener(
            a -> Hyperium.INSTANCE.getHandlers().getBrowserManager().toggleMaximize());
        
        JButton exit = new JButton();
        exit.setBackground(Color.RED);
        exit.setSize(15, 10);
        exit.setBounds(getWidth() - 15, 0, 15, 10);
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.addActionListener(a -> {
            browser.stop();
            Hyperium.INSTANCE.getHandlers().getBrowserManager().setShow(false);
            setVisible(false);
        });
        mp.add(max);
        mp.add(exit);
        container.add(browserView);
        container.add(mp);
        setContentPane(container);
        
    }
}
