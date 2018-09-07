package net.montoyo.mcef.example;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderGuiEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.utilities.Log;
import org.lwjgl.input.Keyboard;

/**
 * An example mod that shows you how to use MCEF.
 * Assuming that it is client-side only and that onInit() is called on initialization.
 * This example shows a simple 2D web browser when pressing F6.
 *
 * @author montoyo
 */
public class ExampleMod implements IDisplayHandler, IJSQueryHandler {

    public static ExampleMod INSTANCE;

    public ScreenCfg hudBrowser = null;
    private HyperiumBind key = new HyperiumBind("Open Browser", Keyboard.KEY_RBRACKET, "key.categories.misc"){
        @Override
        public void onPress() {
            if (!(mc.currentScreen instanceof BrowserScreen)) {
                //Display the web browser UI.
                mc.displayGuiScreen(hasBackup() ? backup : new BrowserScreen());
                backup = null;
            }
        }
    };
    private Minecraft mc = Minecraft.getMinecraft();
    private BrowserScreen backup = null;
    private API api;

    public void onPreInit() {
        //Grab the API and make sure it isn't null.
        api = MCEFApi.getAPI();
        if (api == null)
            return;

        api.registerScheme("mod", ModScheme.class, true, false, false);
    }

    public void onInit() {
        INSTANCE = this;

        //Register key binding and listen to the FML event bus for ticks.
        Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(key);
        //mem leak
        EventBus.INSTANCE.register(this);

        if (api != null) {
            //Register this class to handle onAddressChange and onQuery events
            api.registerDisplayHandler(this);
            api.registerJSQueryHandler(this);
        }
    }

    public void setBackup(BrowserScreen bu) {
        backup = bu;
    }

    public boolean hasBackup() {
        return (backup != null);
    }

    public void showScreen(String url) {
        if (mc.currentScreen instanceof BrowserScreen)
            ((BrowserScreen) mc.currentScreen).loadURL(url);
        else if (hasBackup()) {
            mc.displayGuiScreen(backup);
            backup.loadURL(url);
            backup = null;
        } else
            mc.displayGuiScreen(new BrowserScreen(url));
    }

    public IBrowser getBrowser() {
        if (mc.currentScreen instanceof BrowserScreen)
            return ((BrowserScreen) mc.currentScreen).browser;
        else if (backup != null)
            return backup.browser;
        else
            return null;
    }



    @Override
    public void onAddressChange(IBrowser browser, String url) {
        //Called by MCEF if a browser's URL changes. Forward this event to the screen.
        if (mc.currentScreen instanceof BrowserScreen)
            ((BrowserScreen) mc.currentScreen).onUrlChanged(browser, url);
        else if (hasBackup())
            backup.onUrlChanged(browser, url);
    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {
    }

    @Override
    public void onTooltip(IBrowser browser, String text) {
    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {
    }

    @Override
    public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        if (b != null && query.equalsIgnoreCase("username")) {
            if (b.getURL().startsWith("mod://")) {
                //Only allow MCEF URLs to get the player's username to keep his identity secret

                mc.addScheduledTask(() -> {
                    //Add this to a scheduled task because this is NOT called from the main Minecraft thread...

                    try {
                        String name = mc.getSession().getUsername();
                        cb.success(name);
                    } catch (Throwable t) {
                        cb.failure(500, "Internal error.");
                        Log.warning("Could not get username from JavaScript:");
                        t.printStackTrace();
                    }
                });
            } else
                cb.failure(403, "Can't access username from external page");

            return true;
        }

        return false;
    }

    @Override
    public void cancelQuery(IBrowser b, long queryId) {
    }

    @InvokeEvent
    public void onDrawHUD(RenderGuiEvent ev) {
        if (hudBrowser != null)
            hudBrowser.drawScreen(0, 0, 0.f);
    }

}
