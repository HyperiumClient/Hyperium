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

package cc.hyperium.mods.hgames.minigames;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.displayitems.hyperium.MinigameDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class Walls3 extends Minigame {

    private int timesCalled = 0;
    private int duplicateCounter = 0;
    private String currentMap = "";

    @Override
    public void draw(MinigameDisplay display, int starX, double startY, boolean config) {
        display.setHeight(10);
        display.setWidth(10);

//        ElementRenderer.draw(starX, startY + getMultiplier(), "Testing: " + Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName());
//        ElementRenderer.draw(starX, startY + getMultiplier(), "Map: " + currentMap);
//        ElementRenderer.draw(starX, startY + getMultiplier(), "Testing 3");

        timesCalled = 0;
    }

    private int getMultiplier() {
        return timesCalled++ * 10;
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onChat(IChatComponent message) {
        String line = message.getUnformattedText();
        if (line.startsWith("You are currently playing on ")) {
            currentMap = line.split("You are currently playing on ")[1];
            duplicateCounter = 0;
        } else if (line.equalsIgnoreCase("This command is not available on this server!")) {
            currentMap = "None";
            duplicateCounter = 0;
        }
    }

    @Override
    public void onWorldChange() {
        if (duplicateCounter == 0) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/wtfmap");
        }
        duplicateCounter++;
    }
}
