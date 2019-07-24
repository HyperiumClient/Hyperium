/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.browser;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import cc.hyperium.mods.browser.gui.GuiConfig;
import cc.hyperium.mods.browser.keybinds.BrowserBind;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.*;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * @author Koding
 */
public class BrowserMod extends AbstractMod implements IDisplayHandler, IJSQueryHandler {

    public GuiConfig hudBrowser;
    public volatile Map<Integer, Triple<KeyEvent, KeyEvent, String>> keyPressesMap = new HashMap<>();
    @ConfigOpt
    private String homePage = "https://hyperium.cc";
    public GuiBrowser browserGui;
    private MCEF mcef;
    private API api;
    private GuiBrowser backup;
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
