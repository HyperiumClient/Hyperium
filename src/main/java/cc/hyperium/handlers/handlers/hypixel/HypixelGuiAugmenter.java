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

package cc.hyperium.handlers.handlers.hypixel;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.gui.InitGuiEvent;
import cc.hyperium.event.interact.ActionPerformedEvent;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import cc.hyperium.mixins.client.gui.IMixinGuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.HashMap;
import java.util.function.Consumer;

public class HypixelGuiAugmenter {

    private final HashMap<GuiButton, Consumer<GuiButton>> lobbyAdds = new HashMap<>();

    public HypixelGuiAugmenter() {
        lobbyAdds.put(new GuiButton(500002, 1, 1, 100, 20, "View Friends"), button -> new HypixelFriendsGui().show());
    }

    @InvokeEvent
    public void guiOpen(InitGuiEvent event) {
        GuiScreen gui = event.getScreen();
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            String location = Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation();

            //in a lobby
            if (location.toLowerCase().contains("lobby") && gui instanceof GuiContainer) {
                modifyLobbyGui(gui);
            }
        }
    }

    private void modifyLobbyGui(GuiScreen screen) {
        if (screen == null) return;

        lobbyAdds.keySet().forEach(guiButton -> {
            ((IMixinGuiScreen) screen).getButtonList().add(guiButton);
            guiButton.visible = true;
        });
    }


    @InvokeEvent
    public void actionPerformed(ActionPerformedEvent event) {
        Consumer<GuiButton> guiButtonConsumer = lobbyAdds.get(event.getButton());
        if (guiButtonConsumer != null) guiButtonConsumer.accept(event.getButton());
    }
}
