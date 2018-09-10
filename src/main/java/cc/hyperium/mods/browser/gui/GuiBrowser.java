package cc.hyperium.mods.browser.gui;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author Koding
 */
public class GuiBrowser extends GuiScreen {

    private IBrowser browser = null;
    private GuiButton back = null;
    private GuiButton fwd = null;
    private GuiButton go = null;
    private GuiButton min = null;
    private GuiButton vidMode = null;
    private GuiTextField url = null;
    private String urlToLoad, title;

    private static final String YT_REGEX1 = "^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$";
    private static final String YT_REGEX2 = "^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$";
    private static final String YT_REGEX3 = "^https?://(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_\\-]+)(\\?.+)?$";

    public GuiBrowser() {
        urlToLoad = MCEF.HOME_PAGE;
    }

    public GuiBrowser(String url) {
        urlToLoad = (url == null) ? MCEF.HOME_PAGE : url;
    }

    @Override
    public void initGui() {
        Hyperium.INSTANCE.getModIntegration().getBrowserMod().hudBrowser = null;

        if (browser == null) {
            //Grab the API and make sure it isn't null.
            API api = MCEFApi.getAPI();
            if (api == null)
                return;

            //Create a browser and resize it to fit the screen
            browser = api.createBrowser((urlToLoad == null) ? MCEF.HOME_PAGE : urlToLoad, false);
            urlToLoad = null;
        }

        //Resize the browser if window size changed
        if (browser != null)
            browser.resize(mc.displayWidth, mc.displayHeight - scaleY(30));

        //Create GUI
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        String loc = browser.getURL();
        String vId = null;

        if (loc.matches(YT_REGEX1))
            vId = loc.replaceFirst(YT_REGEX1, "$1");
        else if (loc.matches(YT_REGEX2))
            vId = loc.replaceFirst(YT_REGEX2, "$1");
        else if (loc.matches(YT_REGEX3))
            vId = loc.replaceFirst(YT_REGEX3, "$1");

        if (vId != null) {
            browser.loadURL("https://youtube.com/watch?v=" + vId);
        }

        if (url == null) {
            buttonList.add(back = (new GuiButton(0, 0, 10, 20, 20, "<")));
            buttonList.add(fwd = (new GuiButton(1, 20, 10, 20, 20, ">")));
            buttonList.add(go = (new GuiButton(2, width - 60, 10, 20, 20, "Go")));
            buttonList.add(min = (new GuiButton(3, width - 20, 10, 20, 20, "_")));
            buttonList.add(vidMode = (new GuiButton(4, width - 40, 10, 20, 20, "YT")));
            vidMode.enabled = false;

            url = new GuiTextField(5, fontRendererObj, 40, 10, width - 100, 20);
            url.setMaxStringLength(65535);
            //url.setText("mod://mcef/home.html");
        } else {
            buttonList.add(back);
            buttonList.add(fwd);
            buttonList.add(go);
            buttonList.add(min);
            buttonList.add(vidMode);

            //Handle resizing
            vidMode.xPosition = width - 40;
            go.xPosition = width - 60;
            min.xPosition = width - 20;

            String old = url.getText();
            url = new GuiTextField(5, fontRendererObj, 40, 10, width - 100, 20);
            url.setMaxStringLength(65535);
            url.setText(old);
        }
    }

    public int scaleY(int y) {
        double sy = ((double) y) / ((double) height) * ((double) mc.displayHeight);
        return (int) sy;
    }

    public void loadURL(String url) {
        if (browser == null)
            urlToLoad = url;
        else
            browser.loadURL(url);
    }

    @Override
    public void updateScreen() {
        if (urlToLoad != null && browser != null) {
            browser.loadURL(urlToLoad);
            urlToLoad = null;
        }
    }

    @Override
    public void drawScreen(int i1, int i2, float f) {
        //Render the URL box first because it overflows a bit
        url.drawTextBox();

        //Render buttons
        super.drawScreen(i1, i2, f);

        Gui.drawRect(0, 0, width, 10, new Color(0, 0, 0, 100).getRGB());
        if (title != null)
            fontRendererObj.drawString(title, 5, (10f - fontRendererObj.FONT_HEIGHT) / 2, Color.WHITE.getRGB(), true);

        //Renders the browser if itsn't null
        if (browser != null) {
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            browser.draw(.0d, height, width, 30.d); //Don't forget to flip Y axis.
            GlStateManager.enableDepth();
        }
    }

    @Override
    public void onGuiClosed() {
        //Make sure to close the browser when you don't need it anymore.
        if (Hyperium.INSTANCE.getModIntegration().getBrowserMod().getBackup() == null && browser != null)
            browser.close();

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void handleInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(null);
                return;
            }

            boolean pressed = Keyboard.getEventKeyState();
            boolean focused = url.isFocused();
            char key = Keyboard.getEventCharacter();
            int num = Keyboard.getEventKey();

            if (browser != null && !focused) { //Inject events into browser. TODO: Handle keyboard mods.
                if (key != '.' && key != ';' && key != ',') { //Workaround
                    if (pressed)
                        browser.injectKeyPressed(key, 0);
                    else
                        browser.injectKeyReleased(key, 0);
                }

                if (key != Keyboard.CHAR_NONE)
                    browser.injectKeyTyped(key, 0);
            }

            //Forward event to text box.
            if (!pressed && focused && num == Keyboard.KEY_RETURN)
                actionPerformed(go);
            else if (pressed)
                url.textboxKeyTyped(key, num);
        }

        while (Mouse.next()) {
            int btn = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();

            if (browser != null) { //Inject events into browser. TODO: Handle mods & leaving.
                int y = mc.displayHeight - sy - scaleY(30); //Don't forget to flip Y axis.

                if (wheel != 0)
                    browser.injectMouseWheel(sx, y, 0, 1, wheel);
                else if (btn == -1)
                    browser.injectMouseMove(sx, y, 0, y < 0);
                else
                    browser.injectMouseButton(sx, y, 0, btn + 1, pressed, 1);
            }

            if (pressed) { //Forward events to GUI.
                int x = sx * width / mc.displayWidth;
                int y = height - (sy * height / mc.displayHeight) - 1;

                try {
                    mouseClicked(x, y, btn);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                url.mouseClicked(x, y, btn);
            }
        }
    }

    //Called by ExampleMod when the current browser's URL changes.
    public void onUrlChanged(IBrowser b, String newUrl) {
        if (b == browser && url != null) {
            url.setText(newUrl);
            vidMode.enabled = newUrl.matches(YT_REGEX1) || newUrl.matches(YT_REGEX2) || newUrl.matches(YT_REGEX3);
        }
    }

    //Handle button clicks
    @Override
    protected void actionPerformed(GuiButton src) {
        if (browser == null)
            return;

        if (src.id == 0) {
            browser.goBack();

            String loc = browser.getURL();
            String vId = null;

            if (loc.matches(YT_REGEX1))
                vId = loc.replaceFirst(YT_REGEX1, "$1");
            else if (loc.matches(YT_REGEX2))
                vId = loc.replaceFirst(YT_REGEX2, "$1");
            else if (loc.matches(YT_REGEX3))
                vId = loc.replaceFirst(YT_REGEX3, "$1");

            if (vId != null) {
                browser.goBack();
            }
        } else if (src.id == 1) {
            browser.goForward();

            String loc = browser.getURL();
            String vId = null;

            if (loc.matches(YT_REGEX1))
                vId = loc.replaceFirst(YT_REGEX1, "$1");
            else if (loc.matches(YT_REGEX2))
                vId = loc.replaceFirst(YT_REGEX2, "$1");
            else if (loc.matches(YT_REGEX3))
                vId = loc.replaceFirst(YT_REGEX3, "$1");

            if (vId != null) {
                browser.goForward();
            }
        } else if (src.id == 2)
            browser.loadURL(url.getText());
        else if (src.id == 3) {
            Hyperium.INSTANCE.getModIntegration().getBrowserMod().setBackup(this);
            mc.displayGuiScreen(null);
        } else if (src.id == 4) {
            String loc = browser.getURL();
            String vId = null;
            boolean redo = false;

            if (loc.matches(YT_REGEX1))
                vId = loc.replaceFirst(YT_REGEX1, "$1");
            else if (loc.matches(YT_REGEX2))
                vId = loc.replaceFirst(YT_REGEX2, "$1");
            else if (loc.matches(YT_REGEX3))
                redo = true;

            if (vId != null || redo) {
                Hyperium.INSTANCE.getModIntegration().getBrowserMod().setBackup(this);
                mc.displayGuiScreen(new GuiConfig(browser, vId));
            }
        }
    }

    public void onTitleChanged(IBrowser browser, String title) {
        this.title = title;
    }
}
