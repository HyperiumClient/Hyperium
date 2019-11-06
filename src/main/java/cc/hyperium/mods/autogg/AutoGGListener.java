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
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
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
        if (mod.getConfig().ANTI_GG && invoked && (event.getChat().getUnformattedText().toLowerCase().endsWith("gg") ||
            event.getChat().getUnformattedText().endsWith("Good Game")))
            event.setCancelled(true);

        // Make sure the mod is enabled
        if (!mod.isHypixel() || !mod.getConfig().isToggled() || mod.isRunning() || mod.getTriggers().isEmpty()) {
            return;
        }

        // Double parse to remove hypixel formatting codes
        String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());

        if (mod.getTriggers().stream().anyMatch(unformattedMessage::contains) && unformattedMessage.startsWith(" ")) {
            mod.setRunning(true);
            invoked = true;
            // The GGThread in an anonymous class
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
