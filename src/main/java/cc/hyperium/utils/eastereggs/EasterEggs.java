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
import cc.hyperium.config.Settings;
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
        if (HypixelDetector.getInstance().isHypixel() && Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("lobby") && Settings.SUPERSECRETSETTINGSV2) {
            EntityPlayer player = event.getEntity();
           
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

            //9y0
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("9y0")) {
                renderManager.renderEntitySimple(EasterEgg9y0.renderEvent(player), event.getPartialTicks());
            }
            */

            //Amp
            if (player.getUniqueID().equals("4be70fcd-07f9-497b-ab6d-76e3ad1b41b3")) {

                renderManager.renderEntitySimple(EasterEggAmplifiable.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //boom
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("boomboompower")) {
                renderManager.renderEntitySimple(EasterEggBoomBoomPower.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}
            */

            //Chachy
            if (player.getUniqueID().equals("d7e06198-b78a-435e-8e35-5f2706e254f9")) {
                renderManager.renderEntitySimple(EasterEggChachy.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Coal
            if (player.getUniqueID().equals("1e7ccd18-4cea-4fbf-a52e-b5e2e3092832")) {
                renderManager.renderEntitySimple(EasterEggCoalOres.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Conor
            if (player.getUniqueID().equals("5348cef1-4828-4bed-91a5-15cdeb0b52db")) {
                renderManager.renderEntitySimple(EasterEggConorTheDev.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Cubed
            if (player.getUniqueID().equals("4ad76e80-d3dc-4039-8acf-6464bb7c4a76")) {
                renderManager.renderEntitySimple(EasterEggCubedChaos.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Cube
            if (player.getUniqueID().equals("eac14100-2676-4a6d-9dea-85437b2c133d")) {
                renderManager.renderEntitySimple(EasterEggCubxity.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //False

            if (player.getUniqueID().equals("02f62a6b-e748-4546-b9ff-26e3ab4b1076")) {
                renderManager.renderEntitySimple(EasterEggFalseHonesty.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}


            //JJ
            if (player.getUniqueID().equals("635bbf4c-9212-4903-bb17-29bb1179358d")) {
                renderManager.renderEntitySimple(EasterEggJJ.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}


            //KenWay
            if (player.getUniqueID().equals("e3ccb241-00fd-48e0-b1a3-9066430e7a77")) {
                renderManager.renderEntitySimple(EasterEggKenWay.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Kevin
            if (player.getName().equalsIgnoreCase("8233b1e5-bd74-441b-aa34-ee812263cca9")) {
                renderManager.renderEntitySimple(EasterEggKevin.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //KodingKing
            if (player.getUniqueID().equals("75cff0a1-c5f5-4528-9c9f-48f87570065a")) {
                renderManager.renderEntitySimple(EasterEggKodingKing.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Matt
            if (player.getUniqueID().equals("b191dbfd-b6fe-4669-a948-c0398ef08a7d")) {
                renderManager.renderEntitySimple(EasterEggMatt.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}

            //Sk1er
            //Commented due to lack of mob
            /*
            if (player.getName().equalsIgnoreCase("Sk1er")) {
                renderManager.renderEntitySimple(EasterEggSk1er.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}
            */

            //Zezzo
            if (player.getUniqueID().equals("7c84bd05-f9c5-4bf8-b03c-0963be88ef81")) {
                renderManager.renderEntitySimple(EasterEggZezzo.renderEvent(player), event.getPartialTicks());
            player.setInvisible(true);}
        }
    }
}
