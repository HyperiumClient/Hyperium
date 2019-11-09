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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.cosmetics.deadmau5.Deadmau5Cosmetic;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsHandler {
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();

    public SettingsHandler() {
        try {
            Field earsField = Settings.class.getField("EARS_STATE");

            registerCallback(earsField, o -> {
                boolean yes = ((String) o).equalsIgnoreCase("YES");
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();

                if (self == null) {
                    GeneralChatHandler.instance().sendMessage("Error: Could not update cosmetic state because your purchase profile is not loaded.");
                    return;
                }

                JsonHolder purchaseSettings = self.getPurchaseSettings();

                if (!purchaseSettings.has("deadmau5_cosmetic")) {
                    purchaseSettings.put("deadmau5_cosmetic", new JsonHolder());
                }

                purchaseSettings.optJSONObject("deadmau5_cosmetic").put("enabled", yes);
                NettyClient client = NettyClient.getClient();

                if (client != null) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("ears", yes)));
                }
            });

            String[] hats1 = new String[]{"Tophat", "Fez", "Lego"};
            EnumPurchaseType[] hat2 = new EnumPurchaseType[]{EnumPurchaseType.HAT_TOPHAT, EnumPurchaseType.HAT_FEZ, EnumPurchaseType.HAT_LEGO};

            Field hats = Settings.class.getField("HAT_TYPE");
            customStates.put(hats, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null) {
                    List<String> pur = new ArrayList<>();
                    for (int i = 0; i < hat2.length; i++) {
                        if (self.hasPurchased(hat2[i])) {
                            String s = hats1[i];
                            pur.add(s);
                        }
                    }

                    if (pur.size() > 0) {
                        pur.add("NONE");
                        return pur.toArray(new String[0]);
                    }
                }

                return new String[]{"NOT PURCHASED"};
            });

            registerCallback(hats, o -> {
                NettyClient client = NettyClient.getClient();
                if (client == null) return;
                EnumPurchaseType parse = null;

                for (int i = 0; i < hats1.length; i++) {
                    if (hats1[i].equalsIgnoreCase(o.toString())) {
                        parse = hat2[i];
                    }
                }

                boolean none = o.toString().equalsIgnoreCase("NONE");

                if (parse == null && !none) {
                    GeneralChatHandler.instance().sendMessage("Unable to locate hat type: " + o);
                    return;
                }

                JsonHolder put = new JsonHolder().put("internal", true).put("set_hat", true).put("value", none ? "NONE" : parse.toString());
                ServerCrossDataPacket build = ServerCrossDataPacket.build(put);
                client.write(build);
            });

            Field companion_type = Settings.class.getField("COMPANION_TYPE");

            customStates.put(companion_type, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                List<EnumPurchaseType> types = new ArrayList<>();
                types.add(EnumPurchaseType.DRAGON_COMPANION);
                types.add(EnumPurchaseType.HAMSTER_COMPANION);
                types.removeIf(enumPurchaseType -> !self.hasPurchased(enumPurchaseType));
                List<String> vals = new ArrayList<>();
                vals.add("NONE");
                types.stream().map(EnumPurchaseType::getDisplayName).forEach(vals::add);
                return vals.toArray(new String[0]);
            });

            registerCallback(companion_type, o -> {
                NettyClient client = NettyClient.getClient();
                if (client == null) {
                    return;
                }
                JsonHolder put = new JsonHolder().put("internal", true).put("companion", true).put("type", o.toString().equalsIgnoreCase("NONE") ?
                    "NONE" : EnumPurchaseType.parse(o.toString()).toString());
                ServerCrossDataPacket build = ServerCrossDataPacket.build(put);
                client.write(build);
            });

            customStates.put(earsField, () -> {
                Hyperium instance = Hyperium.INSTANCE;
                HyperiumCosmetics cosmetics1 = instance.getCosmetics();

                if (cosmetics1 != null) {
                    Deadmau5Cosmetic deadmau5Cosmetic = cosmetics1.getDeadmau5Cosmetic();
                    if (deadmau5Cosmetic != null && deadmau5Cosmetic.isSelfUnlocked()) {
                        return new String[]{"YES", "NO"};
                    }
                }

                return new String[]{"NOT PURCHASED"};
            });

            customStates.put(Settings.class.getField("SHOW_BUTT"), () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.BUTT)) {
                    return new String[]{
                        "YES",
                        "NO"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });

            registerCallback(Settings.class.getField("SHOW_BUTT"), o -> {
                boolean yes = !((String) o).equalsIgnoreCase("YES");
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();

                if (self != null) {
                    JsonHolder purchaseSettings = self.getPurchaseSettings();
                    purchaseSettings.put("butt", new JsonHolder());
                    purchaseSettings.optJSONObject("butt").put("disabled", yes);
                }

                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    JsonHolder put = new JsonHolder().put("internal", true).put("butt_disabled", yes);
                    client.write(ServerCrossDataPacket.build(put));
                }
            });

            Field max_particle_string = Settings.class.getField("MAX_PARTICLE_STRING");
            customStates.put(max_particle_string, () -> {
                ParticleOverlay overlay = ParticleOverlay.getOverlay();
                if (overlay.purchased()) {
                    return new String[]{
                        "25",
                        "50",
                        "100",
                        "150",
                        "200",
                        "250",
                        "300"};
                }

                return new String[]{"NOT PURCHASED"};
            });

            Field show_wings_string = Settings.class.getField("SHOW_WINGS");
            customStates.put(show_wings_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.WING_COSMETIC)) {
                    return new String[]{
                        "ON",
                        "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });

            registerCallback(show_wings_string, o -> {
                try {
                    Settings.SHOW_WINGS = String.valueOf(o);
                    // Update on netty.
                    String update = String.valueOf(o);
                    boolean packetUpdate;

                    packetUpdate = update.equalsIgnoreCase("on");

                    ServerCrossDataPacket packet = ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_toggle", packetUpdate));

                    NettyClient client = NettyClient.getClient();
                    if (client != null) client.write(packet);
                } catch (Exception ignored) {

                }
            });

            Field show_dragonhead_string = Settings.class.getField("SHOW_DRAGON_HEAD");
            customStates.put(show_dragonhead_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.DRAGON_HEAD)) {
                    return new String[]{
                        "ON",
                        "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });

            registerCallback(show_dragonhead_string, o -> {
                try {
                    Settings.SHOW_DRAGON_HEAD = String.valueOf(o);
                    String update = String.valueOf(o);

                    boolean packetUpdate;
                    // Update on netty.
                    packetUpdate = update.equalsIgnoreCase("on");

                    ServerCrossDataPacket packet = ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("dragon_head", packetUpdate));

                    NettyClient client = NettyClient.getClient();
                    if (client != null) client.write(packet);
                } catch (Exception ignored) {

                }
            });

            registerCallback(max_particle_string, o -> {
                try {
                    Settings.MAX_PARTICLES = Integer.parseInt(o.toString());
                } catch (Exception ignored) {
                }
            });

            Field flip_type_string = Settings.class.getField("FLIP_TYPE_STRING");
            customStates.put(flip_type_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                return self == null || !self.hasPurchased(EnumPurchaseType.FLIP_COSMETIC) ?
                    new String[]{"NOT PURCHASED"} : new String[]{"FLIP", "ROTATE"};
            });

            registerCallback(flip_type_string, o -> {
                String s = o.toString();
                if (s.equalsIgnoreCase("FLIP")) Settings.flipType = 1;
                else if (s.equalsIgnoreCase("ROTATE")) Settings.flipType = 2;
            });

            registerCallback(Settings.class.getField("WINGS_SCALE"), o -> {
                if (PurchaseApi.getInstance() == null || PurchaseApi.getInstance().getSelf() == null ||
                    PurchaseApi.getInstance().getSelf().getPurchaseSettings() == null) {
                    return;
                }

                Double wingScale = (Double) o;
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();

                if (self == null) {
                    GeneralChatHandler.instance().sendMessage("Error: Could not update cosmetic state because your purchase profile is not loaded.");
                    return;
                }

                JsonHolder purchaseSettings = self.getPurchaseSettings();
                if (!purchaseSettings.has("wings")) purchaseSettings.put("wings", new JsonHolder());
                purchaseSettings.optJSONObject("wings").put("scale", wingScale);

                Settings.WINGS_SCALE = wingScale;
                NettyClient client = NettyClient.getClient();
                if (client != null)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_scale", wingScale)));

            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void registerCallback(Field field, Consumer<Object> callback) {
        callbacks.computeIfAbsent(field, tmp -> new ArrayList<>()).add(callback);
    }

    public HashMap<Field, Supplier<String[]>> getCustomStates() {
        return customStates;
    }

    public HashMap<Field, List<Consumer<Object>>> getcallbacks() {
        return callbacks;
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }
}
