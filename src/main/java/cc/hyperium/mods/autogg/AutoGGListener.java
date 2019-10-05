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
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.victoryroyale.VictoryRoyale;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

/**
 * Main listener for AutoGG
 */
public class AutoGGListener {

    private final AutoGG mod;
    private boolean invoked;


    public AutoGGListener(AutoGG mod) {
        this.mod = mod;
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        invoked = false;
    }

    @InvokeEvent
    public void onChat(final ChatEvent event) {
        if (this.mod.getConfig().ANTI_GG && invoked) {
            if (event.getChat().getUnformattedText().toLowerCase().endsWith("gg") || event.getChat().getUnformattedText().endsWith("Good Game"))
                event.setCancelled(true);
        }
        // Make sure the mod is enabled
        if (
            !this.mod.isHypixel() ||
            !this.mod.getConfig().isToggled() || this.mod.isRunning() || this.mod.getTriggers().isEmpty()) {
            return;
        }

        // Double parse to remove hypixel formatting codes
        String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());

        if (this.mod.getTriggers().stream().anyMatch(unformattedMessage::contains) && unformattedMessage.startsWith(" ")) {
            this.mod.setRunning(true);
            invoked = true;
            // The GGThread in an anonymous class
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                VictoryRoyale.getInstance().gameEnded();
            });
            Multithreading.POOL.submit(() -> {
                try {
                    Thread.sleep(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig().getDelay() * 1000);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/achat " + (mod.getConfig().sayGoodGameInsteadOfGG ? (mod.getConfig().lowercase ?
                        "good game" : "Good Game") : (mod.getConfig().lowercase ? "gg" : "GG")));
                    Thread.sleep(2000L);

                    // We are referring to it from a different thread, thus we need to do this
                    Hyperium.INSTANCE.getModIntegration().getAutoGG().setRunning(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
