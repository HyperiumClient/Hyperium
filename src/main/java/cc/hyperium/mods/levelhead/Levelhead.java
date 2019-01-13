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

package cc.hyperium.mods.levelhead;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.levelhead.commands.LevelHeadCommand;
import cc.hyperium.mods.levelhead.config.LevelheadConfig;
import cc.hyperium.mods.levelhead.renderer.LevelHeadRender;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Levelhead extends AbstractMod {

    public final String MODID = "LEVEL_HEAD";
    public final String VERSION = "5.0";
    public final Map<UUID, LevelheadTag> levelCache = new HashMap<>();
    private final Metadata meta;
    private final Map<UUID, Integer> timeCheck = new HashMap<>();
    public UUID userUuid = null;
    public int count = 1;
    public int wait = 60;
    @ConfigOpt
    private String type = "LEVEL";
    private HashMap<UUID, String> trueValueCache = new HashMap<>();
    private Set<UUID> existedMorethan5Seconds = new HashSet<>();
    private long waitUntil = System.currentTimeMillis();
    private int updates = 0;
    private Sk1erMod mod;
    private LevelheadConfig config;
    private boolean levelHeadInfoFailed = false;
    private JsonHolder types = new JsonHolder();

    public Levelhead() {
        Metadata metadata = new Metadata(this, "LevelHead", "5.0", "Sk1er");

        metadata.setDisplayName(ChatColor.AQUA + "LevelHead");

        this.meta = metadata;
    }

    public int getRGBColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
    }

    public int getRGBDarkColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.2f);
    }

    public AbstractMod init() {
        mod = new Sk1erMod(MODID, VERSION, object -> {
            count = object.optInt("count");
            this.wait = object.optInt("wait", Integer.MAX_VALUE);
            //                GeneralChatHandler.instance().sendMessage("An error occurred whilst loading internal Levelhead info. ");
            this.levelHeadInfoFailed = count == 0 || wait == Integer.MAX_VALUE;
        });
        Multithreading.runAsync(() -> types = new JsonHolder(mod.rawWithAgent("https://api.sk1er.club/levelhead_config")));
        this.mod.checkStatus();
        this.config = new LevelheadConfig();
        Hyperium.CONFIG.register(config);
        register(mod);
        register(this);
        userUuid = UUIDUtil.getClientUUID();
        register(new LevelHeadRender(this), this);

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new LevelHeadCommand(this));

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.meta;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean loadOrRender(EntityPlayer player) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return false;
        if (!this.config.isEnabled())
            return false;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getPotionID() == 14)
                return false;
        }
        if (!renderFromTeam(player))
            return false;
        if (player.riddenByEntity != null)
            return false;
        int min = Math.min(64 * 64, this.config.getRenderDistance() * this.config.getRenderDistance());
        if (player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > min) {
            return false;
        }
        if (!this.existedMorethan5Seconds.contains(player.getUniqueID())) {
            return false;
        }

        if (player.hasCustomName() && player.getCustomNameTag().isEmpty()) {
            return false;
        }
        if (player.isInvisible() || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
            return false;
        if (player.isSneaking())
            return false;
        return player.getAlwaysRenderNameTagForRender() && !player.getName().isEmpty();
    }

    private boolean renderFromTeam(EntityPlayer player) {
        Team team = player.getTeam();
        Team team1 = Minecraft.getMinecraft().thePlayer.getTeam();

        if (team != null) {
            Team.EnumVisible enumVisible = team.getNameTagVisibility();
            switch (enumVisible) {
                case ALWAYS:
                    return true;
                case NEVER:
                    return false;
                case HIDE_FOR_OTHER_TEAMS:
                    return team1 == null || team.isSameTeam(team1);
                case HIDE_FOR_OWN_TEAM:
                    return team1 == null || !team.isSameTeam(team1);
                default:
                    return true;
            }
        }
        return true;
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() || !this.config.isEnabled() || !this.mod.isEnabled()) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();

        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            if (System.currentTimeMillis() < this.waitUntil) {
                if (this.updates > 0) {
                    this.updates = 0;
                }
                return;
            }

            for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                if (!existedMorethan5Seconds.contains(entityPlayer.getUniqueID())) {
                    if (!timeCheck.containsKey(entityPlayer.getUniqueID()))
                        timeCheck.put(entityPlayer.getUniqueID(), 0);
                    int old = timeCheck.get(entityPlayer.getUniqueID());
                    if (old > 100) {
                        existedMorethan5Seconds.add(entityPlayer.getUniqueID());
                    } else if (!entityPlayer.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
                        timeCheck.put(entityPlayer.getUniqueID(), old + 1);
                }

                if (loadOrRender(entityPlayer)) {
                    final UUID uuid = entityPlayer.getUniqueID();
                    if (!levelCache.containsKey(uuid)) {
                        getLevel(uuid);
                    }
                }
            }
        }
    }

    private String trimUuid(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    private void getLevel(final UUID uuid) {
        if (updates >= count) {
            waitUntil = System.currentTimeMillis() + 1000 * wait;
            updates = 0;
            return;
        }
        updates++;
        levelCache.put(uuid, null);
        Multithreading.runAsync(() -> {
            String raw = mod.rawWithAgent(
                "https://api.sk1er.club/levelheadv5/" + trimUuid(uuid) + "/" + type
                    + "/" + trimUuid(Minecraft.getMinecraft().getSession().getProfile().getId()) +
                    "/" + VERSION);
            JsonHolder object = new JsonHolder(raw);
            if (!object.optBoolean("success")) {
                object.put("strlevel", "Error");
            }
            LevelheadTag value = buildTag(object, uuid);
            levelCache.put(uuid, value);
            trueValueCache.put(uuid, object.optString("strlevel"));
        });
        Multithreading.POOL.submit(this::clearCache);
    }

    public LevelheadTag buildTag(JsonHolder object, UUID uuid) {
        LevelheadTag value = new LevelheadTag();
        JsonHolder headerObj = new JsonHolder();
        JsonHolder footerObj = new JsonHolder();
        JsonHolder construct = new JsonHolder();
        //Support for serverside override for Custom Levelhead
        //Apply values from server if present
        if (object.has("header_obj")) {
            headerObj = object.optJSONObject("header_obj");
            headerObj.put("custom", true);
        }
        if (object.has("footer_obj")) {
            footerObj = object.optJSONObject("footer_obj");
            footerObj.put("custom", true);
        }
        if (object.has("header")) {
            headerObj.put("header", object.optString("header"));
            headerObj.put("custom", true);
        }
        try {
            if (object.optInt("level") != Integer.valueOf(object.optString("strlevel"))) {
                footerObj.put("custom", true);
            }
        } catch (Exception ignored) {
            footerObj.put("custom", true);
        }
        //Get CONFIG based values and merge
        headerObj.merge(getHeaderConfig(), false);
        footerObj.merge(getFooterConfig().put("footer", object.optString("strlevel", object.optInt("level") + "")), false);

        //Ensure text values are present
        construct.put("header", headerObj).put("footer", footerObj);
        value.construct(construct);
        return value;
    }

    public JsonHolder getHeaderConfig() {
        JsonHolder holder = new JsonHolder();
        holder.put("chroma", config.isHeaderChroma());
        holder.put("rgb", config.isHeaderRgb());
        holder.put("red", config.getHeaderRed());
        holder.put("green", config.getHeaderGreen());
        holder.put("blue", config.getHeaderBlue());
        holder.put("color", config.getHeaderColor());
        holder.put("alpha", config.getHeaderAlpha());
        holder.put("header", config.getCustomHeader() + ": ");
        return holder;
    }

    public JsonHolder getFooterConfig() {
        JsonHolder holder = new JsonHolder();
        holder.put("chroma", config.isFooterChroma());
        holder.put("rgb", config.isFooterRgb());
        holder.put("color", config.getFooterColor());
        holder.put("red", config.getFooterRed());
        holder.put("green", config.getFooterGreen());
        holder.put("blue", config.getFooterBlue());
        holder.put("alpha", config.getFooterAlpha());
        return holder;
    }

    public String getType() {
        return type;
    }

    public void setType(String s) {
        this.type = s;
    }

    public JsonHolder getTypes() {
        return types;
    }

    public LevelheadTag getLevelString(UUID uuid) {
        return levelCache.getOrDefault(uuid, null);
    }

    //Remote runaway memory leak from storing levels in ram.
    private void clearCache() {
        if (levelCache.size() > Math.max(config.getPurgeSize(), 150)) {
            List<UUID> safePlayers = new ArrayList<>();
            for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
                if (existedMorethan5Seconds.contains(player.getUniqueID())) {
                    safePlayers.add(player.getUniqueID());
                }
            }
            existedMorethan5Seconds.clear();
            existedMorethan5Seconds.addAll(safePlayers);

            for (UUID uuid : levelCache.keySet()) {
                if (!safePlayers.contains(uuid)) {
                    levelCache.remove(uuid);
                }
            }
            System.out.println("Cache cleared!");
        }
    }

    private void register(Object... events) {
        for (Object o : events) {
            EventBus.INSTANCE.register(o);
        }
    }

    public LevelheadConfig getConfig() {
        return config;
    }

    public Sk1erMod getSk1erMod() {
        return mod;
    }

    public HashMap<UUID, String> getTrueValueCache() {
        return trueValueCache;
    }
}
