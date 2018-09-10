package cc.hyperium.mods.browser;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiOpenEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mixinsimp.gui.HyperiumGuiMainMenu;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import cc.hyperium.mods.browser.gui.GuiConfig;
import cc.hyperium.mods.browser.keybinds.BrowserBind;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.MCEFApi;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.input.Keyboard;

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

//        addShortcutKeys();

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Browser", "1.0", "KodingKing");
    }

    private void addShortcutKeys() {
        List<Triple<Integer, String, Character>> pipActions = Arrays.asList(
            new ImmutableTriple<>(Keyboard.KEY_NUMPAD5, "Pause", (char) 0x20),
            new ImmutableTriple<>(Keyboard.KEY_NUMPAD6, "Forward", (char) 0x4D),
            new ImmutableTriple<>(Keyboard.KEY_NUMPAD4, "Back", (char) 0x4B)
        );

        for (Triple<Integer, String, Character> entry : pipActions) {
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(
                new HyperiumBind("Browser PIP " + entry.getMiddle(), entry.getLeft()) {
                    @Override
                    public void onPress() {
                        GuiConfig browser = Hyperium.INSTANCE.getModIntegration()
                            .getBrowserMod().hudBrowser;
                        if (Hyperium.INSTANCE.getModIntegration().getBrowserMod().hudBrowser
                            != null) {
                            browser.browser.injectMouseMove(10, 10, 0, false);
                            browser.browser
                                .injectKeyPressed(entry.getRight(), 0);
                        } else {
                            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler()
                                .sendMessage("You don't have PIP on.");
                        }
                    }

                    @Override
                    public void onRelease() {
                        GuiConfig browser = Hyperium.INSTANCE.getModIntegration()
                            .getBrowserMod().hudBrowser;
                        if (browser
                            != null) {
                            browser.browser
                                .injectKeyReleased(entry.getRight(), 0);
                            browser.browser.injectMouseMove(-10, -10, 0, true);
                        }
                    }
                });
        }
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser) {
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).onUrlChanged(browser, url);
        } else if (backup != null) {
            backup.onUrlChanged(browser, url);
        }
    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser) {
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).onTitleChanged(browser, title);
        }
        if (backup != null) {
            backup.onTitleChanged(browser, title);
        }
    }

    @Override
    public void onTooltip(IBrowser browser, String text) {

    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {

    }

    @Override
    public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent,
        IJSQueryCallback cb) {
        return false;
    }

    @Override
    public void cancelQuery(IBrowser b, long queryId) {

    }

    public void showBrowser(String url) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser) {
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).loadURL(url);
        } else if (backup != null) {
            Minecraft.getMinecraft().displayGuiScreen(backup);
            backup.loadURL(url);
            backup = null;
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new GuiBrowser(url));
        }
    }

    @InvokeEvent
    private void onRenderHud(RenderHUDEvent e) {
        if (hudBrowser != null) {
            hudBrowser.drawScreen(0, 0, e.getPartialTicks());
        }
    }

    @InvokeEvent
    private void onDisconnect(GuiOpenEvent e) {
        if (!(e.getGui() instanceof GuiMainMenu))
            return;
        hudBrowser.browser.close();
        hudBrowser = null;
        backup.browser.close();
        backup = null;
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
