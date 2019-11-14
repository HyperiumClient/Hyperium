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

package cc.hyperium.mods.levelhead;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.levelhead.auth.MojangAuth;
import cc.hyperium.mods.levelhead.command.CustomLevelheadCommand;
import cc.hyperium.mods.levelhead.command.LevelheadCommand;
import cc.hyperium.mods.levelhead.display.AboveHeadDisplay;
import cc.hyperium.mods.levelhead.display.DisplayConfig;
import cc.hyperium.mods.levelhead.display.DisplayManager;
import cc.hyperium.mods.levelhead.display.LevelheadDisplay;
import cc.hyperium.mods.levelhead.purchases.LevelheadPurchaseStates;
import cc.hyperium.mods.levelhead.renderer.AboveHeadRenderer;
import cc.hyperium.mods.levelhead.renderer.LevelheadChatRenderer;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.levelhead.renderer.NullLevelheadTag;
import cc.hyperium.mods.levelhead.util.LevelheadJsonHolder;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Levelhead extends AbstractMod {

    public static final String VERSION = "6.4";

    public UUID userUuid;
    public int count = 100;
    public int wait = 1;
    private long waitUntil = System.currentTimeMillis();
    private int updates;

    private MojangAuth auth;
    private DecimalFormat format = new DecimalFormat("#,###");

    private DisplayManager displayManager;
    private LevelheadPurchaseStates levelheadPurchaseStates = new LevelheadPurchaseStates();

    private LevelheadJsonHolder types = new LevelheadJsonHolder();
    private LevelheadJsonHolder paidData = new LevelheadJsonHolder();
    private LevelheadJsonHolder purchaseStatus = new LevelheadJsonHolder();
    private LevelheadJsonHolder rawPurchases = new LevelheadJsonHolder();

    private static Levelhead instance;

    @Override
    public AbstractMod init() {
        instance = this;

        LevelheadJsonHolder jsonHolder = new LevelheadJsonHolder();

        try {
            jsonHolder = new LevelheadJsonHolder(FileUtils.readFileToString(new File(Hyperium.folder, "levelhead.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        displayManager = new DisplayManager(jsonHolder, new File(Hyperium.folder, "levelhead.json"));

        Multithreading.runAsync(() -> types = new LevelheadJsonHolder(rawWithAgent("https://api.sk1er.club/levelhead_config")));

        Sk1erMod sk1erMod = new Sk1erMod("LEVEL_HEAD", VERSION, object -> {
            count = object.optInt("count");
            wait = object.optInt("wait", Integer.MAX_VALUE);
            if (count == 0 || wait == Integer.MAX_VALUE) {
                Hyperium.LOGGER.warn("An error occurred whilst loading loading internal Levelhead info.");
            }
        });

        sk1erMod.checkStatus();
        auth = new MojangAuth();

        Multithreading.runAsync(() -> {
            auth.auth();
            if (auth.isFailed()) {
                Hyperium.LOGGER.warn("Failed to authenticate with Levelhead: {}", auth.getFailedMessage());
            }
        });

        userUuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new LevelheadCommand());
        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new CustomLevelheadCommand());

        LevelheadChatRenderer levelheadChatRenderer = new LevelheadChatRenderer(this);
        EventBus.INSTANCE.register(this);
        EventBus.INSTANCE.register(levelheadChatRenderer);
        EventBus.INSTANCE.register(new AboveHeadRenderer(this));

        Multithreading.runAsync(this::refreshPurchaseStates);
        Multithreading.runAsync(this::refreshRawPurchases);
        Multithreading.runAsync(this::refreshPaidData);

        return this;
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()
            || displayManager == null
            || displayManager.getMasterConfig() == null
            || !displayManager.getMasterConfig().isEnabled()
            || !Sk1erMod.getInstance().isEnabled()) {
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

            displayManager.tick();
        }
    }

    private String rawWithAgent(String url) {
        Hyperium.LOGGER.debug("Fetching " + url);
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (SK1ER LEVEL HEAD V" + VERSION + ")");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }

        return new LevelheadJsonHolder().put("success", false).put("cause", "API_DOWN").toString();
    }

    public void fetch(UUID uuid, LevelheadDisplay display, boolean allowOverride) {
        if (updates >= count) {
            waitUntil = System.currentTimeMillis() + 1000 * wait;
            updates = 0;
            return;
        }

        updates++;
        display.getCache().put(uuid, new NullLevelheadTag(null));
        String type = display.getConfig().getType();

        if (purchaseStatus.has(type) && !purchaseStatus.optBoolean(type)) {
            LevelheadJsonHolder fakeValue = new LevelheadJsonHolder();
            fakeValue.put("header", "Error");
            fakeValue.put("strlevel", "Item '" + type + "' not purchased. If you believe this is an error, contact Sk1er");
            fakeValue.put("success", true);
            display.getCache().put(uuid, buildTag(fakeValue, uuid, display, allowOverride));
            return;
        }

        Multithreading.runAsync(() -> {
            String raw = rawWithAgent(
                "https://api.sk1er.club/levelheadv5/" + trimUuid(uuid) + "/" + type
                    + "/" + trimUuid(Minecraft.getMinecraft().getSession().getProfile().getId()) +
                    "/" + VERSION + "/" + auth.getHash() + "/" + display.getPosition().name());

            LevelheadJsonHolder object = new LevelheadJsonHolder(raw);
            if (!object.optBoolean("success")) {
                object.put("strlevel", "Error");
            }

            if (!allowOverride) {
                object.put("strlevel", object.optString("level"));
                object.remove("header_obj");
                object.remove("footer_obj");
            }

            LevelheadTag value = buildTag(object, uuid, display, allowOverride);
            display.getCache().put(uuid, value);
            display.getTrueValueCache().put(uuid, object.optString("strlevel"));
        });

        Multithreading.POOL.submit(this::clearCache);
    }

    private LevelheadTag buildTag(LevelheadJsonHolder object, UUID uuid, LevelheadDisplay display, boolean allowOverride) {
        LevelheadTag value = new LevelheadTag(uuid);
        LevelheadJsonHolder headerObj = new LevelheadJsonHolder();
        LevelheadJsonHolder footerObj = new LevelheadJsonHolder();
        LevelheadJsonHolder construct = new LevelheadJsonHolder();
        if (object.has("header_obj") && allowOverride) {
            headerObj = object.optJsonObject("header_obj");
            headerObj.put("custom", true);
        }

        if (object.has("footer_obj") && allowOverride) {
            footerObj = object.optJsonObject("footer_obj");
            footerObj.put("custom", true);
        }

        if (object.has("header") && allowOverride) {
            headerObj.put("header", object.optString("header"));
            headerObj.put("custom", true);
        }

        headerObj.merge(display.getHeaderConfig(), !allowOverride);
        footerObj.merge(display.getFooterConfig().put("footer", object.optString("strlevel", format.format(object.getInt("level")))), !allowOverride);
        construct.put("exclude", object.optBoolean("exclude"));
        construct.put("header", headerObj).put("footer", footerObj);
        construct.put("exclude", object.optBoolean("exclude"));
        construct.put("custom", object.optJsonObject("custom"));
        value.construct(construct);
        return value;
    }

    public HashMap<String, String> allowedTypes() {
        List<String> keys = types.getKeys();
        HashMap<String, String> data = keys.stream().collect(Collectors.toMap(key -> key, key -> types.optJsonObject(key).optString("name"), (a, b) -> b, HashMap::new));
        LevelheadJsonHolder stats = paidData.optJsonObject("stats");
        stats.getKeys().stream().filter(s -> purchaseStatus.optBoolean(s)).forEach(s -> data.put(s, stats.optJsonObject(s).optString("name")));
        return data;
    }

    private synchronized void refreshRawPurchases() {
        rawPurchases = new LevelheadJsonHolder(rawWithAgent("https://api.sk1er.club/purchases/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString()));
    }

    private synchronized void refreshPaidData() {
        paidData = new LevelheadJsonHolder(rawWithAgent("https://api.sk1er.club/levelhead_data"));
    }

    public synchronized void refreshPurchaseStates() {
        purchaseStatus = new LevelheadJsonHolder(rawWithAgent("https://api.sk1er.club/levelhead_purchase_status/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString()));
        levelheadPurchaseStates.setChat(purchaseStatus.optBoolean("chat"));
        levelheadPurchaseStates.setTab(purchaseStatus.optBoolean("tab"));
        levelheadPurchaseStates.setExtraHead(purchaseStatus.optInt("head"));

        DisplayManager displayManager = this.displayManager;

        while (displayManager.getAboveHead().size() <= levelheadPurchaseStates.getExtraHead()) {
            displayManager.getAboveHead().add(new AboveHeadDisplay(new DisplayConfig()));
        }

        displayManager.adjustIndexes();
    }

    private void clearCache() {
        displayManager.checkCacheSizes();
    }

    private String trimUuid(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static Levelhead getInstance() {
        return instance;
    }

    public LevelheadJsonHolder getTypes() {
        return types;
    }

    public MojangAuth getAuth() {
        return auth;
    }

    public int getRGBColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
    }

    public int getDarkRGBColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.2f);
    }

    public LevelheadPurchaseStates getLevelheadPurchaseStates() {
        return levelheadPurchaseStates;
    }

    public LevelheadJsonHolder getPurchaseStatus() {
        return purchaseStatus;
    }

    public LevelheadJsonHolder getRawPurchases() {
        return rawPurchases;
    }

    public LevelheadJsonHolder getPaidData() {
        return paidData;
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public String getVersion() {
        return "v" + VERSION;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Levelhead", VERSION, "Sk1er");
    }
}
