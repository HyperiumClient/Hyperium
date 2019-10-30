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

package cc.hyperium.mods.autogg;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.autogg.commands.GGCommand;
import cc.hyperium.mods.autogg.config.AutoGGConfig;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The AutoGG mod by 2pi
 *
 * @author 2Pi, Amplifiable, boomboompower
 */
public class AutoGG extends AbstractMod {

    // Static woo
    private static List<String> triggers;
    private final Metadata meta;
    private AutoGGConfig config;
    private boolean running;

    public AutoGG() {
        Metadata metadata = new Metadata(this, "AutoGG", "2.0", "2Pi");
        metadata.setDisplayName(ChatColor.GOLD + "AutoGG");

        meta = metadata;

        running = false;
    }

    @Override
    public AbstractMod init() {
        config = new AutoGGConfig();

        EventBus.INSTANCE.register(new AutoGGListener(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler()
            .registerCommand(new GGCommand(this));

        // The GetTriggers class
        Multithreading.POOL.submit(() -> {
            try {
                final String rawTriggers = IOUtils.toString(
                    new URL("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/triggers.txt"),
                    StandardCharsets.UTF_8
                );

                triggers = new ArrayList<>(Arrays.asList(rawTriggers.split("\n")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }
    public AutoGGConfig getConfig() {
        return config;
    }
    public boolean isRunning() {
        return running;
    }
    public void setRunning(final boolean running) {
        this.running = running;
    }
    public List<String> getTriggers() {
        return triggers;
    }
    public void setTriggers(final ArrayList<String> triggersIn) {
        triggers = triggersIn;
    }
    public boolean isHypixel() {
        return HypixelDetector.getInstance().isHypixel();
    }
}
