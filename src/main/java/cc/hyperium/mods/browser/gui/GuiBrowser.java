package cc.hyperium.mods.browser.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.UpdateUtil;
import cc.hyperium.config.Settings;
import cc.hyperium.mods.browser.util.BrowserUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.apache.commons.lang3.tuple.Triple;
import org.cef.OS;
import org.cef.browser.CefBrowserOsr;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.lang.Exception;

/**
 * @author Koding
 */
public class GuiBrowser extends GuiScreen {

    public static IBrowser browser = null;
    private static GuiConfig persistentConfigGui;
    private GuiButton back = null;
    private GuiButton fwd = null;
    private GuiButton go = null;
    private GuiButton close = null;
    private GuiButton pip = null;
    private GuiTextField url = null;
    private String urlToLoad, title;

    public GuiBrowser(String url) {
        urlToLoad = (url == null) ? MCEF.HOME_PAGE : url;
    }

    @Override
    public void initGui() {
        if (Settings.BROWSER_DOWNLOAD) {
            Minecraft.getMinecraft().guiAchievement.clearAchievements();
            Hyperium.INSTANCE.getModIntegration().getBrowserMod().hudBrowser = null;
            this.url = new GuiTextField(5, fontRendererObj, 40, 10, width - 100, 20);
            this.url.setMaxStringLength(65535);

            if (browser == null) {
                //Grab the API and make sure it isn't null.
                API api = MCEFApi.getAPI();
                if (api == null) {
                    return;
                }

                //Create a browser and resize it to fit the screen
                browser = api.createBrowser((urlToLoad == null) ? MCEF.HOME_PAGE : urlToLoad, false);
                urlToLoad = null;
            }

            //Resize the browser if window size changed
            if (browser != null) {
                browser.resize(mc.displayWidth, mc.displayHeight - scaleY(30));
            }

            ((CefBrowserOsr) browser).setZoomLevel(1.0);

            //Create GUI
            Keyboard.enableRepeatEvents(true);
            buttonList.clear();

            if (url == null) {
                buttonList.add(back = (new GuiButton(0, 0, 10, 20, 20, "<")));
                buttonList.add(fwd = (new GuiButton(1, 20, 10, 20, 20, ">")));
                buttonList.add(go = (new GuiButton(2, width - 60, 10, 20, 20, "Go")));
                buttonList.add(close = (new GuiButton(3, width - 20, 10, 20, 20, "X")));
                buttonList.add(pip = (new GuiButton(4, width - 40, 10, 20, 20, "PIP")));
                pip.enabled = true;


            } else {
                if (back == null) {
                    back = (new GuiButton(0, 0, 10, 20, 20, "<"));
                }
                buttonList.add(back);
                if (fwd == null) {
                    fwd = (new GuiButton(1, 20, 10, 20, 20, ">"));
                }
                buttonList.add(fwd);
                if (go == null) {
                    go = (new GuiButton(2, width - 60, 10, 20, 20, "Go"));
                }
                buttonList.add(go);
                if (close == null) {
                    close = (new GuiButton(3, width - 20, 10, 20, 20, "X"));
                }
                buttonList.add(close);
                if (pip == null) {
                    pip = (new GuiButton(4, width - 40, 10, 20, 20, "PIP"));
                }
                buttonList.add(pip);
                //Handle resizing
                pip.xPosition = width - 40;
                go.xPosition = width - 60;
                close.xPosition = width - 20;

                String old = url.getText();
                url = new GuiTextField(5, fontRendererObj, 40, 10, width - 100, 20);
                url.setMaxStringLength(65535);
                url.setText(old);
            }
            if (persistentConfigGui == null) {
                persistentConfigGui = new GuiConfig(browser);
                Hyperium.CONFIG.register(persistentConfigGui);
            }
        } else {
            buttonList.add(new GuiButton(1337, width / 2 - 100, height / 2 - 10, 200, 20, "Restart and download"));
            buttonList.add(new GuiButton(1338, width / 2 - 100, height / 2 - -12, 200, 20, "Cancel"));
        }
    }

    public int scaleY(int y) {
        double sy = ((double) y) / ((double) height) * ((double) mc.displayHeight);
        return (int) sy;
    }

    public void loadURL(String url) {
        if (url != null) {
            this.url.setText(url);
        }
        if (browser == null) {
            urlToLoad = url;
        } else {
            if (OS.isMacintosh()) {
                Hyperium.INSTANCE.getModIntegration().getBrowserMod().browserGui.urlToLoad = url;
                GuiBrowser.browser.close();
                GuiBrowser.browser = null;
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(Hyperium.INSTANCE.getModIntegration().getBrowserMod().browserGui);
            } else {
                browser.loadURL(url);
            }
        }
    }

    @Override
    public void updateScreen() {
        if (urlToLoad != null && browser != null) {
            browser.loadURL(urlToLoad);
            urlToLoad = null;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Settings.BROWSER_DOWNLOAD) {
            //Render the URL box first because it overflows a bit
            if (url != null) {
                url.drawTextBox();
            }

            //Render buttons

            Gui.drawRect(0, 0, width, 10, new Color(0, 0, 0, 100).getRGB());
            if (title != null) {
                fontRendererObj.drawString(title, 5, (10f - fontRendererObj.FONT_HEIGHT) / 2, Color.WHITE.getRGB(),
                    true);
            }

            //Renders the browser if itsn't null
            if (browser != null) {
                GlStateManager.disableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                browser.draw(.0d, height, width, 30.d); //Don't forget to flip Y axis.
                GlStateManager.enableDepth();
            }
        } else
            drawCenteredString(fontRendererObj, "In order for browser to work, the client needs to download some required files", width / 2, height / 2 - 50, 0xffffffff);
    }

    @Override
    public void onGuiClosed() {
        //Make sure to close the browser when you don't need it anymore.
//        if (Hyperium.INSTANCE.getModIntegration().getBrowserMod().getBackup() == null
//                && browser != null) {
//            browser.close();
//        }

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        //Stop escape from breaking the menu if you need to press it.
    }

    @Override
    public void handleInput() {
        if (Settings.BROWSER_DOWNLOAD) {
            Map<Integer, Triple<KeyEvent, KeyEvent, String>> map = Hyperium.INSTANCE.getModIntegration().getBrowserMod().keyPressesMap;
            while (Keyboard.next()) {

                boolean pressed = Keyboard.getEventKeyState();
                boolean focused = url.isFocused();
                char key = Keyboard.getEventCharacter();
                int num = Keyboard.getEventKey();

                if (num == Keyboard.KEY_L && BrowserUtil.getModifierInt() == 2 && pressed) {
                    url.setFocused(true);
                    url.setSelectionPos(0);
                    return;
                }
                if (browser != null
                    && !focused) { //Inject events into browser.
                    if (key != '.' && key != ';' && key != ',') { //Workaround
                        if (pressed) {
                            browser.injectKeyPressed(key, BrowserUtil.getModifierInt());
                        } else {
                            browser.injectKeyReleased(key, BrowserUtil.getModifierInt());
                        }
                    }

                    if (map.containsKey(KeyEvent.getExtendedKeyCodeForChar(key))) {
                        Triple<KeyEvent, KeyEvent, String> entry = map.get(KeyEvent.getExtendedKeyCodeForChar(key));
                        if (pressed) {
                            KeyEvent keyEvent = new KeyEvent(
                                ((CefBrowserOsr) browser).getUIComponent(), KeyEvent.KEY_TYPED, 0, 0, 0,
                                (char) 0);
                            ((CefBrowserOsr) browser).injectKeyEvent(entry.getLeft());
                            ((CefBrowserOsr) browser).injectKeyEvent(keyEvent);
                        } else {
                            ((CefBrowserOsr) browser).injectKeyEvent(entry.getMiddle());
                        }
                    }

                    if (key != Keyboard.CHAR_NONE) {
                        browser.injectKeyTyped(key, BrowserUtil.getModifierInt());
                    }
                }

                //Forward event to text box.
                if (!pressed && focused && num == Keyboard.KEY_RETURN) {
                    actionPerformed(go);
                } else if (pressed) {
                    url.textboxKeyTyped(key, num);
                }

            }
        }
        while (Mouse.next()) {
            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();

            if (browser != null) { //Inject events into browser. TODO: Handle mods & leaving.
                int y = mc.displayHeight - sy - scaleY(30); //Don't forget to flip Y axis.

                if (wheel != 0) {
                    browser.injectMouseWheel(sx, y, BrowserUtil.getModifierInt(), 1, wheel);
                } else if (btn == -1) {
                    browser.injectMouseMove(sx, y, BrowserUtil.getModifierInt(), y < 0);
                } else {
                    browser.injectMouseButton(sx, y, BrowserUtil.getModifierInt(), btn + 1, pressed, 1);
                }
            }

            if (pressed) { //Forward events to GUI.
                int x = sx * width / mc.displayWidth;
                int y = height - (sy * height / mc.displayHeight) - 1;

                try {
                    mouseClicked(x, y, btn);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                if (url != null)
                    url.mouseClicked(x, y, btn);
            }
        }
    }

    //Called by ExampleMod when the current browser's URL changes.
    public void onUrlChanged(IBrowser b, String newUrl) {
        if (b == browser && url != null) {
            url.setText(newUrl);
        }
    }

    //Handle button clicks
    @Override
    protected void actionPerformed(GuiButton src) {
        try {
            if (src.id == 1338) {
                mc.displayGuiScreen(null);
            }
            if (src.id == 1337) {
                Settings.BROWSER_DOWNLOAD = true;
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        Runtime.getRuntime().exec(Hyperium.INSTANCE.getLaunchCommand(true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                Minecraft.getMinecraft().shutdown();
            }
            if (browser == null) {
                return;
            }
            switch (src.id) {
                case 0:
                    browser.goBack();
                    break;
                case 1:
                    browser.goForward();
                    break;
                case 2:
                    String text = url.getText();
                    if (!text.contains(".")) {
                        text = "http://google.com/search?q=" + URLEncoder.encode(text);
                        url.setText(text);
                    }
                    this.loadURL(text);
                    break;
                case 3:
                    Hyperium.INSTANCE.getModIntegration().getBrowserMod().setBackup(null);
                    mc.displayGuiScreen(null);
                    break;
                case 4:
                    Hyperium.INSTANCE.getModIntegration().getBrowserMod().setBackup(this);
                    browser.resize(GuiConfig.width, GuiConfig.height);
                    GuiConfig.drawSquare = true;
                    mc.displayGuiScreen(persistentConfigGui);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTitleChanged(IBrowser browser, String title) {
        this.title = title;
    }


}
