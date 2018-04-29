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

package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

public class CommandDebug implements BaseCommand {

    private static final Gson printer = new GsonBuilder().setPrettyPrinting().create();

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
        StringBuilder builder = new StringBuilder();
        HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
        builder.append("\n");
        builder.append("Purchase callback: ");
        builder.append(printer.toJson(self.getResponse().getObject()));
        builder.append("\n");
        builder.append("\n");
        builder.append("Hypixel: ").append(HypixelDetector.getInstance().isHypixel());
        builder.append("\n");
        builder.append("Badlion: ").append(HypixelDetector.getInstance().isBadlion());
        builder.append("\n");
        builder.append("\n");
        Hyperium.CONFIG.save();
        builder.append("Config: ").append(printer.toJson(Hyperium.CONFIG.getConfig()));
        builder.append("\n");
        builder.append("\n");

        builder.append("ChromaHUD: ").append(printer.toJson(ChromaHUDApi.getInstance().getConfig().getObject()));
        builder.append("\n");
        builder.append("\n");
        builder.append("Keybinds: ").append(printer.toJson(Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeyBindConfig().getKeyBindJson().getData()));
        builder.append("\n");
        builder.append("\n");
        builder.append("Location: ").append(Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation());
        builder.append("\n");
        builder.append("\n");
        builder.append("Levelhead");
        builder.append("\n");

        builder.append("Count: ").append(((Levelhead) Hyperium.INSTANCE.getModIntegration().getLevelhead()).count).append("\n");
        builder.append("Wait: ").append(((Levelhead) Hyperium.INSTANCE.getModIntegration().getLevelhead()).wait).append("\n");
        builder.append("Hypixel: ").append(HypixelDetector.getInstance().isHypixel()).append("\n");
        builder.append("Remote Status: ").append(Sk1erMod.getInstance().isEnabled()).append("\n");
        builder.append("Local Stats: ").append(HypixelDetector.getInstance().isHypixel()).append("\n");
        builder.append("Header State: ").append(((Levelhead) Hyperium.INSTANCE.getModIntegration().getLevelhead()).getHeaderConfig()).append("\n");
        builder.append("Footer State: ").append(((Levelhead) Hyperium.INSTANCE.getModIntegration().getLevelhead()).getFooterConfig()).append("\n");
        builder.append("Callback: ").append(Sk1erMod.getInstance().getResponse()).append("\n");

        String message = builder.toString();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message), null);
        GeneralChatHandler.instance().sendMessage("Data copied to clipboard. Please paste in hastebin.com (This has been opened), save and send in Discord");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL("https://hastebin.com").toURI());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
