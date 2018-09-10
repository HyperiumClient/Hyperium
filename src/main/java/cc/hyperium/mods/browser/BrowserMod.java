package cc.hyperium.mods.browser;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import cc.hyperium.mods.browser.gui.GuiConfig;
import cc.hyperium.mods.browser.keybinds.BrowserBind;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.*;

/**
 * @author Koding
 */
public class BrowserMod extends AbstractMod implements IDisplayHandler, IJSQueryHandler {

    private MCEF mcef;
    private API api;
    private GuiBrowser backup;
    public GuiConfig hudBrowser;

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);

        api = MCEFApi.getAPI();

        mcef = new MCEF();
        mcef.init();

        Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(new BrowserBind());

        if (api != null) {
            api.registerDisplayHandler(this);
            api.registerJSQueryHandler(this);
        }

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Browser", "1.0", "KodingKing");
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser)
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).onUrlChanged(browser, url);
        else if (backup != null)
            backup.onUrlChanged(browser, url);
    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser)
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).onTitleChanged(browser, title);
        if (backup != null)
            backup.onTitleChanged(browser, title);
    }

    @Override
    public void onTooltip(IBrowser browser, String text) {

    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {

    }

    @Override
    public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        return false;
    }

    @Override
    public void cancelQuery(IBrowser b, long queryId) {

    }

    public void showBrowser(String url) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser)
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).loadURL(url);
        else if (backup != null) {
            Minecraft.getMinecraft().displayGuiScreen(backup);
            backup.loadURL(url);
            backup = null;
        } else
            Minecraft.getMinecraft().displayGuiScreen(new GuiBrowser(url));
    }

    @InvokeEvent
    private void onRenderHud(RenderHUDEvent e) {
        if (hudBrowser != null)
            hudBrowser.drawScreen(0, 0, e.getPartialTicks());
    }

    public MCEF getMcef() {
        return mcef;
    }

    public GuiBrowser getBackup() {
        return backup;
    }

    public void setBackup(GuiBrowser backup) {
        this.backup = backup;
    }

    public API getApi() {
        return api;
    }
}
