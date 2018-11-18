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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

// Created by RDIL on November 18th, 2018.  
// Created in hope to limit toxicity.  

package cc.hyperium.integrations.BetterChatFilter;

import cc.hyperium.Hyperium;

import cc.hyperium.config.Settings;

import cc.hyperium.event.ChatEvent;

import cc.hyperium.event.InvokeEvent;

import net.minecraft.client.Minecraft;

public class BetterChatFilter {
  
  // Sorry about this next line, just needed somewhere to store the bad words themselves D: 
  private static final String[] reallyBadWordsOwO = {"fuck", "shit", "bitch", "cunt", "goddamn", "godsdamn", "damn", "nigga", "nigger", "twat", "arse", "ass", "hell", "crap"};
  
  @InvokeEvent
  public void onChat(ChatEvent e) {
    
    if (Settings.BETTER_CHAT_FILTER) {
      
      for(int i = 0; i < reallyBadWordsOwO.length(); i++;) {
        
        if(e.getChat().getUnformattedText().contains(reallyBadWordsOwO[i])) { e.setCancelled(true); }
      
      }
      
      return;
      
    } else {
      
      return;
    
    }
    
  }

}
