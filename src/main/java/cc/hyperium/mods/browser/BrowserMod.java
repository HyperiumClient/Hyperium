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

package cc.hyperium.mods.browser;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import cc.hyperium.mods.browser.gui.GuiConfig;
import cc.hyperium.mods.browser.keybinds.BrowserBind;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.MCEFApi;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.input.Keyboard;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Koding
 */
public class BrowserMod extends AbstractMod implements IDisplayHandler, IJSQueryHandler {

    public GuiConfig hudBrowser;
    public volatile Map<Integer, Triple<KeyEvent, KeyEvent, String>> keyPressesMap = new HashMap<>();
    @ConfigOpt
    public String homePage = "https://hyperium.cc";
    public GuiBrowser browserGui;
    private MCEF mcef;
    private API api;
    private GuiBrowser backup;
    private int currentKey = 0;
    private MutableTriple<KeyEvent, KeyEvent, String> currentKeyTriple;
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

        registerCommands();
        Multithreading.runAsync(() -> {
            if (Settings.BROWSER_DOWNLOAD) {
                long start = System.currentTimeMillis();
                JFrame jFrame = new JFrame("Hyperium Keycode Initializer (Please ignore)");
                jFrame.add(new JPanel());
                jFrame.setSize(300, 300);

                JTextField jTextField = new JTextField();
                jTextField.setSize(300, 300);

                jTextField.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (keyPressesMap.containsKey(currentKey)) {
                            return;
                        }
                        try {
                            currentKeyTriple.setLeft(e);
                        } catch (NullPointerException ex) {
                            currentKeyTriple.setLeft(e);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (keyPressesMap.containsKey(currentKey)) {
                            return;
                        }
                        try {
                            currentKeyTriple.setMiddle(e);
                        } catch (NullPointerException ex) {
                            currentKeyTriple.setMiddle(e);
                        }
                    }
                });
                jFrame.add(jTextField);
                jFrame.setVisible(true);
                jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                jFrame.requestFocus();
                jTextField.requestFocus();

                try {
                    int[] keyPressList = new int[]{KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE,
                        KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
                    Robot robot = AccessController.doPrivileged(
                        (PrivilegedExceptionAction<Robot>) () -> new Robot(
                            jTextField.getGraphicsConfiguration().getDevice()));

                    robot.mouseMove(0, 0);
                    try {
                        robot.keyPress(KeyEvent.VK_QUOTE);
                        robot.keyRelease(KeyEvent.VK_QUOTE);
                    } catch (IllegalArgumentException e) {
                        try {
                            robot.keyPress(KeyEvent.VK_SHIFT);
                            robot.keyPress(KeyEvent.VK_2);
                            robot.keyRelease(KeyEvent.VK_2);
                            robot.keyRelease(KeyEvent.VK_SHIFT);
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    jFrame.setAlwaysOnTop(true);

                    end:
                    for (int key : keyPressList) {

                        currentKeyTriple = new MutableTriple<>();
                        currentKey = key;

                        jFrame.toFront();
                        jFrame.setState(Frame.NORMAL);

                        robot.keyPress(key);
                        robot.keyRelease(key);
                        while (currentKeyTriple.getLeft() == null
                            || currentKeyTriple.getMiddle() == null) {
                            if (System.currentTimeMillis() - start < 2000) {
                                break end;
                            }
                            Thread.sleep(1L);
                        }
                        keyPressesMap.put(key, currentKeyTriple);
                    }
                    currentKey = 0;
                    currentKeyTriple = null;
                    jFrame.dispose();
                } catch (PrivilegedActionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

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
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(backup);
            backup.loadURL(null);
        } else {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(browserGui);
        }
    }

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
