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

package cc.hyperium.mods.levelhead.display;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.levelhead.config.MasterConfig;
import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;
import cc.hyperium.utils.ChatColor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayManager {

    private Gson GSON = new Gson();
    private List<AboveHeadDisplay> aboveHead = new ArrayList<>();
    private LevelheadDisplay chat;
    private TabDisplay tab;
    private MasterConfig config = new MasterConfig();
    private File file;

    public DisplayManager(LevelheadJsonHolder source, File file) {
        if (source == null) source = new LevelheadJsonHolder();

        this.file = file;

        if (source.has("master")) {
            try {
                config = GSON.fromJson(source.optJsonObject("master").getObject(), MasterConfig.class);
            } catch (Exception ignored) {
            }
        }

        if (config == null) {
            config = new MasterConfig();
            Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cCould not load previous Levelhead settings! If this is your first time, nothing is wrong."));
        }

        for (JsonElement head : source.optJSONArray("head")) {
            try {
                aboveHead.add(new AboveHeadDisplay(GSON.fromJson(head.getAsJsonObject(), DisplayConfig.class)));
            } catch (Exception ignored) {
            }
        }

        if (source.has("chat")) {
            try {
                chat = new ChatDisplay(GSON.fromJson(source.optJsonObject("chat").getObject(), DisplayConfig.class));
            } catch (Exception ignored) {
            }
        }

        if (source.has("tab")) {
            try {
                tab = new TabDisplay(GSON.fromJson(source.optJsonObject("tab").getObject(), DisplayConfig.class));
            } catch (Exception ignored) {
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::save));

        if (aboveHead.isEmpty()) aboveHead.add(new AboveHeadDisplay(new DisplayConfig()));

        if (tab == null) {
            DisplayConfig config = new DisplayConfig();
            config.setType("QUESTS");
            tab = new TabDisplay(config);
        }

        adjustIndexes();

        if (chat == null) {
            DisplayConfig config = new DisplayConfig();
            config.setType("GUILD_NAME");
            chat = new ChatDisplay(config);
        }
    }

    public void adjustIndexes() {
        int bound = aboveHead.size();
        for (int i = 0; i < bound; i++) {
            aboveHead.get(i).setBottomValue(i == 0);
            aboveHead.get(i).setIndex(i);
        }
    }

    public void tick() {
        if (!config.isEnabled()) return;

        aboveHead.forEach(LevelheadDisplay::tick);

        if (tab != null) tab.tick();
        if (chat != null) chat.tick();
    }

    public void checkCacheSizes() {
        aboveHead.forEach(LevelheadDisplay::checkCacheSize);

        if (tab != null) tab.checkCacheSize();
        if (chat != null) chat.checkCacheSize();
    }

    public void save() {
        LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder();
        jsonHolder.put("master", new LevelheadJsonHolder(GSON.toJson(config)));

        if (tab != null) jsonHolder.put("tab", new LevelheadJsonHolder(GSON.toJson(tab.getConfig())));
        if (chat != null) jsonHolder.put("chat", new LevelheadJsonHolder(GSON.toJson(chat.getConfig())));
        JsonArray head = new JsonArray();
        aboveHead.stream().map(aboveHeadDisplay -> new LevelheadJsonHolder(GSON.toJson(aboveHeadDisplay.getConfig())).getObject()).forEach(head::add);
        jsonHolder.put("head", head);

        try {
            FileUtils.writeStringToFile(file, jsonHolder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        aboveHead.forEach(aboveHeadDisplay -> {
            aboveHeadDisplay.cache.clear();
            aboveHeadDisplay.trueValueCache.clear();
        });

        if (tab != null) {
            tab.cache.clear();
            tab.trueValueCache.clear();
        }

        if (chat != null) {
            chat.cache.clear();
            chat.trueValueCache.clear();
        }
    }

    public List<AboveHeadDisplay> getAboveHead() {
        return aboveHead;
    }

    public LevelheadDisplay getChat() {
        return chat;
    }

    public LevelheadDisplay getTab() {
        return tab;
    }

    public MasterConfig getMasterConfig() {
        return config;
    }
}
