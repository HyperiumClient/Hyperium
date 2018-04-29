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

package cc.hyperium.purchases;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.packages.*;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PurchaseApi {

    public final static String url = "https://api.hyperium.cc/purchases/";
    private static final PurchaseApi instance = new PurchaseApi();
    private final ConcurrentHashMap<UUID, HyperiumPurchase> purchasePlayers = new ConcurrentHashMap<>();
    private final HashMap<EnumPurchaseType, Class<? extends AbstractHyperiumPurchase>> purchaseClasses = new HashMap<>();
    private final HashMap<String, UUID> nameToUuid = new HashMap<>();

    private PurchaseApi() {
        register(EnumPurchaseType.WING_COSMETIC, WingCosmetic.class);
        register(EnumPurchaseType.KILL_TRACKER_MUSCLE, KillTrackerMuscles.class);
        register(EnumPurchaseType.DAB_ON_KILL, DabOnKill.class);
        register(EnumPurchaseType.PARTICLE_BACKGROUND, ParticleBackgroundCosmetic.class);
        register(EnumPurchaseType.FLIP_COSMETIC, FlipCosmeticPackage.class);
        register(EnumPurchaseType.DEADMAU5_COSMETIC, EarsCosmetic.class);
        getPackageAsync(Minecraft.getMinecraft().getSession().getProfile().getId(), hyperiumPurchase -> {
            System.out.println("Loaded self packages: " + hyperiumPurchase.getResponse());
        });

    }

    public static PurchaseApi getInstance() {
        return instance;
    }

    @InvokeEvent
    public void worldSwitch(SpawnpointChangeEvent event) {
        Multithreading.runAsync(() -> {
            synchronized (instance) {
                UUID id = Minecraft.getMinecraft().getSession().getProfile().getId();
                HyperiumPurchase purchase = purchasePlayers.get(id);
                purchasePlayers.clear();
                if (purchase != null) {
                    purchasePlayers.put(id, purchase);
                }
                System.out.println("Cleared purchase cache (" + purchasePlayers.size() + ")");
            }
        });

    }

    public UUID nameToUUID(String name) {
        UUID uuid = nameToUuid.get(name.toLowerCase());
        if (uuid != null)
            return uuid;
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null)
            return null;
        for (EntityPlayer playerEntity : theWorld.playerEntities) {
            if (playerEntity.getName().equalsIgnoreCase(name) || EnumChatFormatting.getTextWithoutFormattingCodes(playerEntity.getDisplayName().getUnformattedText()).equalsIgnoreCase(name)) {
                nameToUuid.put(name.toLowerCase(), playerEntity.getUniqueID());
                return playerEntity.getUniqueID();
            }
        }
        return null;
    }

    public HyperiumPurchase getPackageSync(UUID uuid) {
        if (uuid == null)
            return null;
        return purchasePlayers.computeIfAbsent(uuid, uuid1 -> new HyperiumPurchase(uuid, get(url + uuid.toString())));
    }

    public HyperiumPurchase getPackageIfReady(UUID uuid) {
        if (uuid == null)
            return null;
        return purchasePlayers.get(uuid);
    }

    public void getPackageAsync(UUID uuid, Consumer<HyperiumPurchase> callback) {
        Multithreading.runAsync(() -> callback.accept(getPackageSync(uuid)));
    }

    public HyperiumPurchase getSelf() {
        return getPackageIfReady(Minecraft.getMinecraft().getSession().getProfile().getId());
    }

    public void ensureLoaded(UUID uuid) {
        Multithreading.runAsync(() -> getPackageSync(uuid));
    }

    public void register(EnumPurchaseType type, Class<? extends AbstractHyperiumPurchase> ex) {
        purchaseClasses.put(type, ex);
    }

    public AbstractHyperiumPurchase parse(EnumPurchaseType type, JsonHolder data) {
        Class<? extends AbstractHyperiumPurchase> c = purchaseClasses.get(type);
        if (c == null) {
            throw new NullPointerException(type + " doesn't have a class? ");
        }
        Class[] cArg = new Class[2];
        cArg[0] = EnumPurchaseType.class;
        cArg[1] = JsonHolder.class;
        try {
            Constructor<? extends AbstractHyperiumPurchase> declaredConstructor = c.getDeclaredConstructor(cArg);
            return declaredConstructor.newInstance(type, data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    JsonHolder get(String url) {
        url = url.replace(" ", "%20");
        System.out.println("Fetching " + url);
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return new JsonHolder(IOUtils.toString(is, Charset.forName("UTF-8")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return new JsonHolder(object);
    }

}
