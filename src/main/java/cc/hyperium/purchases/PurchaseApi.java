package cc.hyperium.purchases;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.purchases.packages.CustomLevelhead;
import cc.hyperium.purchases.packages.EarlyBird;
import cc.hyperium.utils.JsonHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseApi {

    public final static String url = "https://api.hyperium.cc/purchases/";
    private static final PurchaseApi instance = new PurchaseApi();
    private ConcurrentHashMap<UUID, HyperiumPurchase> purchasePlayers = new ConcurrentHashMap<>();
    private HashMap<EnumPurchaseType, Class<? extends AbstractHyperiumPurchase>> purchaseClasses = new HashMap<>();

    private PurchaseApi() {
        register(EnumPurchaseType.LEVEL_HEAD, CustomLevelhead.class);
        register(EnumPurchaseType.EARLY_BIRD, EarlyBird.class);
    }

    public static PurchaseApi getInstance() {
        return instance;
    }

    public HyperiumPurchase getPackageSync(UUID uuid) {
        return purchasePlayers.computeIfAbsent(uuid, uuid1 -> new HyperiumPurchase(uuid, get(url + uuid.toString())));
    }

    public void ensureLoaded(UUID uuid) {
        Multithreading.runAsync(() -> getPackageSync(uuid));
    }

    public void register(EnumPurchaseType type, Class<? extends AbstractHyperiumPurchase> ex) {
        purchaseClasses.put(type, ex);
    }

    public AbstractHyperiumPurchase parse(EnumPurchaseType type, JsonHolder data) {
        Class<? extends AbstractHyperiumPurchase> c = purchaseClasses.get(type);
        Class[] cArg = new Class[2];
        cArg[0] = EnumPurchaseType.class;
        cArg[1] = JsonHolder.class;
        try {
            return c.getDeclaredConstructor(cArg).newInstance(type, data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    JsonHolder get(String url) {
        return new JsonHolder(Sk1erMod.getInstance().rawWithAgent(url));
    }

}
