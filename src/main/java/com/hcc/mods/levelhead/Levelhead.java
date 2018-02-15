package com.hcc.mods.levelhead;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.mods.sk1ercommon.Multithreading;
import com.hcc.mods.levelhead.config.LevelheadConfig;
import com.hcc.mods.levelhead.renderer.LevelHeadRender;
import com.hcc.mods.levelhead.renderer.LevelheadTag;
import com.hcc.mods.sk1ercommon.Sk1erMod;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Levelhead {

    public static final String MODID = "LEVEL_HEAD";
    public static final String VERSION = "HCC_1.0-4.1.2";
    private static Levelhead instance;
    public Map<UUID, LevelheadTag> levelCache = new HashMap<>();
    public UUID userUuid = null;
    public int count = 1;
    public int wait = 60;
    private long waitUntil = System.currentTimeMillis();
    private int updates = 0;
    private Sk1erMod mod;
    private LevelheadConfig config;
    private HashMap<UUID, Integer> trueLevelCache = new HashMap<>();
    private java.util.List<UUID> probablyNotFakeWatchdogBoi = new ArrayList<>();
    private HashMap<UUID, Integer> timeCheck = new HashMap<>();

    public Levelhead() {
        init();
    }

    /*
    Made with a
     */
    public static int getRGBColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
    }

    public static int getRGBDarkColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.2f);
    }

    public static Levelhead getInstance() {
        return instance;
    }

    public void init() {
        mod = new Sk1erMod(MODID, VERSION, object -> {
            count = object.optInt("count");
            this.wait = object.optInt("wait", Integer.MAX_VALUE);
            if (count == 0 || wait == Integer.MAX_VALUE) {
                GeneralChatHandler.instance().sendMessage("An error occurred whilst loading internal Levelhead info. ");
            }
        });
        mod.checkStatus();
        config = new LevelheadConfig();
        HCC.config.register(config);
        register(mod);
        instance = this;
        userUuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        register(new LevelHeadRender(this), this);
    }


    public boolean loadOrRender(EntityPlayer player) {
        if (!HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel())
            return false;
        if (!config.isEnabled())
            return false;

        for (PotionEffect effect : player.getActivePotionEffects()) { // TODO - Method obfuscated (PORTING REQUIRED)
            if (effect.getPotionID() == 14)
                return false;
        }
        if (!renderFromTeam(player))
            return false;
        if (player.riddenByEntity != null)
            return false;
        int min = Math.min(64 * 64, config.getRenderDistance() * config.getRenderDistance());
        if (player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > min) {
            return false;
        }
        if (!probablyNotFakeWatchdogBoi.contains(player.getUniqueID())) {
            return false;
        }

        if (player.hasCustomName() && player.getCustomNameTag().isEmpty()) {
            return false;
        }
        if (player.isInvisible() || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
            return false;
        if (player.isSneaking())
            return false;
        return player.getAlwaysRenderNameTagForRender() && !player.getDisplayName().getUnformattedText().isEmpty();


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

        if (!HCC.INSTANCE.getHandlers().getHypixelDetector().isHypixel() || !config.isEnabled() || !mod.isEnabled()) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            if (System.currentTimeMillis() < waitUntil) {
                if (updates > 0) {
                    updates = 0;
                }
                return;
            }

            for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                if (!probablyNotFakeWatchdogBoi.contains(entityPlayer.getUniqueID())) {
                    if (!timeCheck.containsKey(entityPlayer.getUniqueID()))
                        timeCheck.put(entityPlayer.getUniqueID(), 0);
                    int old = timeCheck.get(entityPlayer.getUniqueID());
                    if (old > 100) {
                        if (!probablyNotFakeWatchdogBoi.contains(entityPlayer.getUniqueID()))
                            probablyNotFakeWatchdogBoi.add(entityPlayer.getUniqueID());
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

    public String rawWithAgent(String url) {
        return getSk1erMod().rawWithAgent(url);
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
            JsonHolder object = new JsonHolder(rawWithAgent("http://sk1er.club/newlevel/" + uuid.toString().replace("-", "") + "/" + VERSION));
            LevelheadTag value = buildTag(object, uuid);
            levelCache.put(uuid, value);
            trueLevelCache.put(uuid, object.optInt("level"));
        });
        Multithreading.POOL.submit(this::clearCache);
    }

    public LevelheadTag buildTag(JsonHolder object, UUID uuid) {
        LevelheadTag value = new LevelheadTag(uuid);
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
        //Get config based values and merge
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

    public LevelheadTag getLevelString(UUID uuid) {
        return levelCache.getOrDefault(uuid, null);
    }

    //Remote runaway memory leak from storing levels in ram.
    //TODO make configurable for people with more ram
    private void clearCache() {
        if (levelCache.size() > Math.max(config.getPurgeSize(), 150)) {
            ArrayList<UUID> safePlayers = new ArrayList<>();
            for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
                if (probablyNotFakeWatchdogBoi.contains(player.getUniqueID())) {
                    safePlayers.add(player.getUniqueID());
                }
            }
            probablyNotFakeWatchdogBoi.clear();
            probablyNotFakeWatchdogBoi.addAll(safePlayers);

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

    public HashMap<UUID, Integer> getTrueLevelCache() {
        return trueLevelCache;
    }
}
