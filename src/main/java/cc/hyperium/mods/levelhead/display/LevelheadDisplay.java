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

import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;
import cc.hyperium.utils.ChatColor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LevelheadDisplay {

    protected final ConcurrentHashMap<UUID, LevelheadTag> cache = new ConcurrentHashMap<>();
    final ConcurrentHashMap<UUID, String> trueValueCache = new ConcurrentHashMap<>();
    final List<UUID> existedMoreThan5Seconds = new ArrayList<>();
    final HashMap<UUID, Integer> timeCheck = new HashMap<>();

    private DisplayPosition position;
    private DisplayConfig config;

    public LevelheadDisplay(DisplayPosition position, DisplayConfig config) {
        this.position = position;
        this.config = config;
    }

    public LevelheadJsonHolder getHeaderConfig() {
        LevelheadJsonHolder holder = new LevelheadJsonHolder();
        holder.put("chroma", config.isHeaderChroma());
        holder.put("rgb", config.isHeaderRgb());

        holder.put("red", config.getHeaderRed());
        holder.put("green", config.getHeaderGreen());
        holder.put("blue", config.getHeaderBlue());
        holder.put("alpha", config.getHeaderAlpha());

        holder.put("color", config.getHeaderColor());
        holder.put("header", config.getCustomHeader() + ": ");
        return holder;
    }

    public LevelheadJsonHolder getFooterConfig() {
        LevelheadJsonHolder holder = new LevelheadJsonHolder();
        holder.put("chroma", config.isFooterChroma());
        holder.put("rgb", config.isFooterRgb());

        holder.put("red", config.getFooterRed());
        holder.put("green", config.getFooterGreen());
        holder.put("blue", config.getFooterBlue());
        holder.put("alpha", config.getFooterAlpha());

        holder.put("color", config.getFooterColor());
        return holder;
    }

    public boolean loadOrRender(EntityPlayer player) {
        return !player.getDisplayName().getFormattedText().contains(ChatColor.COLOR_CHAR + "k");
    }

    public DisplayPosition getPosition() {
        return position;
    }
    public DisplayConfig getConfig() {
        return config;
    }

    public ConcurrentHashMap<UUID, LevelheadTag> getCache() {
        return cache;
    }
    public ConcurrentHashMap<UUID, String> getTrueValueCache() {
        return trueValueCache;
    }
    public List<UUID> getExistedMoreThan5Seconds() {
        return existedMoreThan5Seconds;
    }
    public HashMap<UUID, Integer> getTimeCheck() {
        return timeCheck;
    }

    public abstract void tick();
    public abstract void checkCacheSize();
    public abstract void onDelete();
}
