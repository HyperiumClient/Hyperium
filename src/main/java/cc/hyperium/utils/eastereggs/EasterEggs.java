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

package cc.hyperium.utils.eastereggs;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.utils.eastereggs.eastereggplayerdata.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

/**
 * EasterEggs thingy
 *
 * @author SHARDcoder
 */
public class EasterEggs {
    @InvokeEvent
    private void onRender(RenderPlayerEvent event) {
        if (HypixelDetector.getInstance().isHypixel() && Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("lobby")) {
            EntityPlayer player = event.getEntity();
            player.setInvisible(true);
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

            //9y0
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("9y0")) {
                renderManager.renderEntitySimple(EasterEgg9y0.renderEvent(player), event.getPartialTicks());
        }
            */

            //Amp
            if (player.getName().equalsIgnoreCase("Amplifiable")) {
                renderManager.renderEntitySimple(EasterEggAmplifiable.renderEvent(player), event.getPartialTicks());
            }

            //boom
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("boomboompower")) {
                renderManager.renderEntitySimple(EasterEggBoomBoomPower.renderEvent(player), event.getPartialTicks());
            }
            */

            //Chachy
            if (player.getName().equalsIgnoreCase("ChachyOreo")) {
                renderManager.renderEntitySimple(EasterEggChachy.renderEvent(player), event.getPartialTicks());
            }

            //Coal
            if (player.getName().equalsIgnoreCase("CoalOres")) {
                renderManager.renderEntitySimple(EasterEggCoalOres.renderEvent(player), event.getPartialTicks());
            }

            //Conor
            if (player.getName().equalsIgnoreCase("ConorTheDabLord")) {
                renderManager.renderEntitySimple(EasterEggConorTheDev.renderEvent(player), event.getPartialTicks());
            }

            //Cubed
            if (player.getName().equalsIgnoreCase("U_9")) {
                renderManager.renderEntitySimple(EasterEggCubedChaos.renderEvent(player), event.getPartialTicks());
            }

            //Cube
            if (player.getName().equalsIgnoreCase("Cubxity")) {
                renderManager.renderEntitySimple(EasterEggCubxity.renderEvent(player), event.getPartialTicks());
            }

            //False
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggFalseHonesty.renderEvent(player), event.getPartialTicks());
            }
            */

            //JJ
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggJJ.renderEvent(player), event.getPartialTicks());
            }
            */

            //KenWay
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggKenWay.renderEvent(player), event.getPartialTicks());
            }
            */

            //Kevin
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggKevin.renderEvent(player), event.getPartialTicks());
            }
            */

            //KodingKing
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggKodingKing.renderEvent(player), event.getPartialTicks());
            }
            */

            //Matt
            //Commented due to lack of IGN
            /*
            if (player.getName().equalsIgnoreCase("")) {
                renderManager.renderEntitySimple(EasterEggMatt.renderEvent(player), event.getPartialTicks());
            }
            */

            //Sk1er
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("Sk1er")) {
                renderManager.renderEntitySimple(EasterEggSk1er.renderEvent(player), event.getPartialTicks());
            }
            */

            //Zezzo
            if (player.getName().equalsIgnoreCase("Zezzo")) {
                renderManager.renderEntitySimple(EasterEggZezzo.renderEvent(player), event.getPartialTicks());
            }
        }
    }
}
