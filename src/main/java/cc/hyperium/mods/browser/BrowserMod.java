package cc.hyperium.mods.browser;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import cc.hyperium.mods.browser.gui.GuiConfig;
import cc.hyperium.mods.browser.keybinds.BrowserBind;
import net.minecraft.client.Minecraft;
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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * @author Koding
 */
public class BrowserMod extends AbstractMod implements IDisplayHandler, IJSQueryHandler {

    private MCEF mcef;
    private API api;
    private GuiBrowser backup;
    public GuiConfig hudBrowser;

    @ConfigOpt
    public String homePage = "https://hyperium.cc";

    private Queue<Runnable> delayedRunnableQueue = new ArrayDeque<>();

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
        browserGui = new GuiBrowser(homePage);

//        addShortcutKeys();
        registerCommands();

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Browser", "1.0", "KodingKing");
    }

    private void registerCommands() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(
            new BaseCommand() {
                @Override
                public String getName() {
                    return "browse";
                }

                @Override
                public String getUsage() {
                    return "browse <url>";
                }

                @Override
                public void onExecute(String[] args) throws CommandException {
                    if (args.length == 0) {
                        throw new CommandException("Enter a URL to browse to.");
                    } else {
                        delayedRunnableQueue.add(() -> {
                            showBrowser();

                            String url = String.join("%20", args);
                            if (backup == null) {
                                browserGui.loadURL(url);
                            } else {
                                backup.loadURL(url);
                            }
                        });
                    }
                }
            });

        List<Triple<String, String, String>> commands = Arrays.asList(
            new ImmutableTriple<>("google", "Enter a search query.",
                "https://google.com/search?q=%QUERY%"),
            new ImmutableTriple<>("youtube", "Enter a search query.",
                "https://youtube.com/search?q=%QUERY%")
        );

        for (Triple<String, String, String> command : commands) {
            Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(
                new BaseCommand() {
                    @Override
                    public String getName() {
                        return command.getLeft();
                    }

                    @Override
                    public String getUsage() {
                        return getName() + " <query>";
                    }

                    @Override
                    public void onExecute(String[] args) throws CommandException {
                        if (args.length == 0) {
                            throw new CommandException(command.getMiddle());
                        } else {
                            delayedRunnableQueue.add(() -> {
                                showBrowser();

                                String url = command.getRight()
                                    .replace("%QUERY%", String.join("%20", args));
                                if (backup == null) {
                                    browserGui.loadURL(url);
                                } else {
                                    backup.loadURL(url);
                                }
                            });
                        }
                    }
                });
        }
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

    public void showBrowser() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser) {
            ((GuiBrowser) Minecraft.getMinecraft().currentScreen).loadURL(homePage);
        } else if (backup != null) {
            Minecraft.getMinecraft().displayGuiScreen(backup);
            backup.loadURL(null);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(browserGui);
        }
    }

    public GuiBrowser browserGui;

    @InvokeEvent
    private void onRenderHud(RenderHUDEvent e) {
        if (hudBrowser != null) {
            hudBrowser.drawScreen(0, 0, e.getPartialTicks());
        }
    }

    @InvokeEvent
    private void onTick(TickEvent e) {
        Runnable toRun = delayedRunnableQueue.poll();
        if (toRun == null) {
            return;
        }
        toRun.run();
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
