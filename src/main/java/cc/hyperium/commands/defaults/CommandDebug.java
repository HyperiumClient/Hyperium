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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.network.NetworkHandler;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.kbrewster.mojangapi.MojangAPI;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandDebug implements BaseCommand {

    private static final Gson printer = new GsonBuilder().setPrettyPrinting().create();

    private static boolean isPremium;

    private static void tryChromaHUD(StringBuilder builder) {
        try {
            builder.append("ChromaHUD: ").append(printer.toJson(ChromaHUDApi.getInstance().getConfig().getObject()));
        } catch (Exception e) {
            builder.append("ChromaHUD: Error");
        }
    }

    private static void tryConfig(StringBuilder builder) {
        try {
            Hyperium.CONFIG.save();
            builder.append("Config: ").append(printer.toJson(Hyperium.CONFIG.getConfig()));
        } catch (Exception e) {
            builder.append("Config: Error");
        }
    }

    private static void tryKeybinds(StringBuilder builder) {
        try {
            builder.append("Keybinds: ").append(printer.toJson(Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeyBindConfig().getKeyBindJson().getData()));
        } catch (Exception e) {
            builder.append("Keybinds: Error");
        }
    }

    private static void tryLevelhead(StringBuilder builder) {
        try {
            builder.append("Count: ").append(Hyperium.INSTANCE.getModIntegration().getLevelhead().count).append("\n");
            builder.append("Wait: ").append(Hyperium.INSTANCE.getModIntegration().getLevelhead().wait).append("\n");
            builder.append("Hypixel: ").append(HypixelDetector.getInstance().isHypixel()).append("\n");
            builder.append("Remote Status: ").append(Sk1erMod.getInstance().isEnabled()).append("\n");
            builder.append("Local Stats: ").append(HypixelDetector.getInstance().isHypixel()).append("\n");
            builder.append("Callback: ").append(Sk1erMod.getInstance().getResponse()).append("\n");
            builder.append("Callback_types: ").append(Levelhead.getInstance().getTypes()).append("\n");
        } catch (Exception e) {
            builder.append("Levelhead: Error");
        }
    }

    public static String get() {
        StringBuilder builder = new StringBuilder();
        PurchaseApi api = PurchaseApi.getInstance();

        if (api == null) return "";

        builder.append("\n\n");

        Multithreading.runAsync(CommandDebug::checkUUID);
        if (isPremium) {
            builder.append("Premium: True, ").append("UUID is ").append(Minecraft.getMinecraft().thePlayer.getGameProfile().getId());
        } else {
            builder.append("Premium: False, user doesn't have a UUID");
        }

        builder.append("\n\n");

        HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
        builder.append("Purchase callback: ");
        if (self != null) {
            JsonHolder response = self.getResponse();
            if (response != null)
                builder.append(printer.toJson(response.getObject()));
        }

        builder.append("\n\n");

        HypixelDetector instance = HypixelDetector.getInstance();
        if (instance != null) builder.append("Hypixel: ").append(instance.isHypixel());

        builder.append("\n\n\n\n");

        NetworkHandler networkHandler = Hyperium.INSTANCE.getNetworkHandler();
        if (networkHandler != null) {
            List<String> verboseLogs = networkHandler.getVerboseLogs();
            verboseLogs.forEach(verboseLog -> {
                builder.append(verboseLog);
                builder.append("\n");
            });

            builder.append(verboseLogs);
            builder.append("\n");
        }

        tryConfig(builder);

        builder.append("\n\n");
        tryChromaHUD(builder);

        builder.append("\n\n");
        tryKeybinds(builder);

        builder.append("\n\n");
        tryLevelhead(builder);

        builder.append("\n\n");
        builder.append("Levelhead");

        builder.append("\n");

        return builder.toString();
    }


    @Override
    public String getName() {
        return "hyperium_debug";
    }

    @Override
    public String getUsage() {
        return "Usage: /hyperium_debug";
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("log")) {
            Hyperium.INSTANCE.getNetworkHandler().setLog(true);
            GeneralChatHandler.instance().sendMessage("Enable logging, please restart your game to begin. It will be auto disabled after the next launch");
        }
    }

    private static void checkUUID() {
        try {
            MojangAPI.getProfile(Minecraft.getMinecraft().getSession().getProfile().getId());
            isPremium = true;
        } catch (Exception e) {
            isPremium = false;
        }
    }
}
