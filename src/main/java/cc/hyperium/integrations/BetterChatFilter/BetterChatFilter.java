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

// Created by RDIL in November 2018.  
// Created in hope to limit toxicity.  

package cc.hyperium.integrations.BetterChatFilter;
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
 *  Better chat filter
 *  For chat filtering.  
 *  Its kinda self explanitory
 */

public class BetterChatFilter {
  // private static list for badwords
  private static List<String> badwords;
  
  // Set file URL
  private static final String badWordsURL = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/BadWords.txt";
  
  // try to download file from hyperium repo
  try {
    private final String rawBadwords = IOUtils.toString(new URL(badWordsURL), Charset.forName("UTF-8"));
    badWords = new ArrayList<>(Arrays.asList(rawBadwords.split("\n")));
  } catch (Exception depressionHits) {
    System.out.println("[BetterChatFilter] Failed to download bad word file");
    depressionHits.printStackTrace(); // happens upon depression
  }
  
  // thing to get stuff
  public List<String> getBadwords() {return badwords;}
  
  @InvokeEvent
  public void onChat(ChatEvent chatty) { 
    String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());
    if(getBadwords().stream().anyMatch(unformattedMessage::contains) && Settings.BETTER_CHAT_FILTER) {
      chatty.setCancelled(true);
    }
  } 
}
