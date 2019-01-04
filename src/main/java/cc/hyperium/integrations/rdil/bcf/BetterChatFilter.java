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

// Created in hope to limit toxicity.  

package cc.hyperium.integrations.betterchatfilter;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import net.minecraft.client.Minecraft;
import cc.hyperium.utils.ChatColor;
import java.lang.*;
import java.lang.System;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

/*
 *  Better chat filter.  
 *  For chat filtering. 
 *  @author RDIL
 */
public class BetterChatFilter {
  private List<String> badwords;
  
  /*
   *  URL that bad word list is located
   */
  private static final String BAD_WORDS_URL = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/BadWords.txt";
  
  /*
   * Download file from Hyperium-Repo
   */
  public BetterChatFilter() {
    try {
      static final String rawBadwords = Arrays.asList(IOUtils.toString(new URL(BAD_WORDS_URL), Charset.forName("UTF-8")).split("\n");
    } catch (Exception e) {
      // After failing to download file, make arraylist to make up for it.  
      ArrayList<String> rawBadwords = new ArrayList<String>();
      rawBadwords.add("fuck");
      rawBadwords.add("shit");
    }
  }
  
  public ArrayList<String> getBadwords() {
    return rawBadwords;
  }

  protected int getArrayListSize() {
    return rawBadwords.size();
  }
  
  @InvokeEvent
  public void onChat(ChatEvent e) { 
    String unformattedMessage = ChatColor.stripColor(e.getChat().getUnformattedText());
    for(int i = 0; i < getArrayListSize(); i++) {
      if(unformattedMessage.containsIgnoreCase(getBadwords().get(i)) && Settings.BETTER_CHAT_FILTER) {
        e.setCancelled(true);
      } else {
        e.setCancelled(false);
      }
    }
  } 
} 
