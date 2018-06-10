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

package me.semx11.autotip;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.command.AutotipCommand;
import me.semx11.autotip.command.LimboCommand;
import me.semx11.autotip.command.TipHistoryCommand;
import me.semx11.autotip.event.ChatListener;
import me.semx11.autotip.event.HypixelListener;
import me.semx11.autotip.event.Tipper;
import me.semx11.autotip.misc.AutotipThreadFactory;
import me.semx11.autotip.util.FileUtil;
import me.semx11.autotip.util.Hosts;
import me.semx11.autotip.util.MessageOption;
import me.semx11.autotip.util.Version;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Autotip extends AbstractMod {

    public static final String MODID = "autotip";
    public static final String VERSION_STRING = "2.0.3";
    public static final Version VERSION = new Version(VERSION_STRING);
    public static final ExecutorService THREAD_POOL = Executors
            .newCachedThreadPool(new AutotipThreadFactory());
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final List<String> alreadyTipped = new ArrayList<>();
    public static String USER_DIR = "";
    public static MessageOption messageOption = MessageOption.SHOWN;
    public static String playerUUID = "";
    public static boolean onHypixel = false;
    public static boolean toggle = true;
    public static int totalTipsSent;
    /**
     * The metadata of Autotip
     */
    private final Metadata meta;

    public Autotip() {
        Metadata metadata = new Metadata(this, "Autotip", "2.0.3", "Semx11, 2pi, Sk1er");

        metadata.setDisplayName(ChatColor.AQUA + "Autotip");

        this.meta = metadata;
    }

    public Autotip init() {
        try {
            playerUUID = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
            USER_DIR = "mods" + File.separator + "autotip" + File.separator + playerUUID
                    + File.separator;
            this.registerEvents(
                    new Tipper(),
                    new HypixelListener(),
                    new ChatListener()
            );
            this.registerCommands(
                    new AutotipCommand()

            );
            this.registerCommands(
                    new TipHistoryCommand());
            this.registerCommands(
                    new LimboCommand()
            );

            FileUtil.getVars();
            Hosts.updateHosts();
        } catch (NullPointerException e2) {
            Hyperium.LOGGER.debug("[Auto-GG] Invalid UUID detected; Not logged in?.");
        }
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.meta;
    }

    private void registerEvents(Object... events) {
        Arrays.asList(events).forEach(EventBus.INSTANCE::register);
    }

    private void registerCommands(BaseCommand... commands) {
        Arrays.asList(commands).forEach((e) -> Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(e));
    }

}