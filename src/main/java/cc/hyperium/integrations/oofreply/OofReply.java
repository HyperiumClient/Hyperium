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

// Created by RDIL on November 13th, 2018.  

package cc.hyperium.integrations.oofreply;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;

import net.minecraft.client.Minecraft;

public class OofReply {
  
  private static final String oofByOtherPlayer = "oof";
  private static final String selfOOF = "OOF!";
  
  @InvokeEvent
  public void onChat(ChatEvent e) {
    if (e.getChat().getUnformattedText().contains(oofByOtherPlayer) && Settings.OOF_REPLY) {
      Minecraft.getMinecraft().thePlayer.sendChatMessage(selfOOF);
    }
  
  }

}
