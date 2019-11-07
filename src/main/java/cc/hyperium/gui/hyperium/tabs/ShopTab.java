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

package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.CapesGui;
import cc.hyperium.gui.ParticleGui;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.components.ButtonComponent;
import cc.hyperium.gui.hyperium.components.CollapsibleTabComponent;
import cc.hyperium.gui.hyperium.components.LabelComponent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ShopTab extends AbstractTab {

    private JsonHolder personData;
    private JsonHolder cosmeticCallback;

    public ShopTab(HyperiumMainGui gui) {
        super(gui, "tab.shop.name");
        refreshData();
    }

    private void refreshData() {
        Multithreading.runAsync(() -> {
            PurchaseApi.getInstance().refreshSelf();
            personData = PurchaseApi.getInstance().getSelf().getResponse();
            cosmeticCallback = PurchaseApi.getInstance()
                .get("https://api.hyperium.cc/cosmetics/" + Objects.requireNonNull(UUIDUtil
                    .getClientUUID()).toString().replace("-", ""));
            components.clear();
            addComponents();
        });
    }

    private void addComponents() {
        CollapsibleTabComponent infoTab = new CollapsibleTabComponent(this, new ArrayList<>(),
            "Info");
        infoTab.setCollapsed(false);

        infoTab.addChild(new LabelComponent(this, new ArrayList<>(),
            "Total Credits: " + personData.optInt("total_credits")));
        infoTab.addChild(new LabelComponent(this, new ArrayList<>(),
            "Remaining Credits: " + personData.optInt("remaining_credits")));
        infoTab.addChild(new ButtonComponent(this, new ArrayList<>(), "Capes",
            () -> Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new CapesGui())));
        infoTab.addChild(new ButtonComponent(this, new ArrayList<>(), "Particles",
            () -> new ParticleGui().show()));
        infoTab.addChild(new ButtonComponent(this, new ArrayList<>(), "Open in browser",
            () -> {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null) {
                    try {
                        desktop.browse(new URI("https://hyperium.sk1er.club"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }));
        infoTab.addChild(new ButtonComponent(this, new ArrayList<>(), "Purchase credits",
            () -> {
                Desktop desktop = Desktop.getDesktop();
                if (desktop != null) {
                    try {
                        desktop.browse(new URI("https://purchase.sk1er.club/category/1125808"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }));
        infoTab
            .addChild(new ButtonComponent(this, new ArrayList<>(), "Refresh", this::refreshData));

        CollapsibleTabComponent purchaseTab = new CollapsibleTabComponent(this, Arrays.asList("Purchase", "Shop"), "Purchase");

        cosmeticCallback.getKeys().forEach(key -> {
            JsonHolder cosmetic = cosmeticCallback.optJSONObject(key);
            if (cosmetic.optBoolean("cape") || key.toLowerCase().startsWith("particle") || cosmetic
                .optString("name").toLowerCase().endsWith("animation")) {
                return;
            }

            CollapsibleTabComponent purchase = new CollapsibleTabComponent(this, new ArrayList<>(), cosmetic.optString("name"));
            purchase.addChild(new LabelComponent(this, new ArrayList<>(), cosmetic.optString("name") + " (" + cosmetic.optInt("cost") + " credits)"));
            purchase.addChild(new LabelComponent(this, new ArrayList<>(), cosmetic.optString("description")));
            purchase.addChild(new ButtonComponent(this, new ArrayList<>(),
                cosmetic.optBoolean("purchased") ? "PURCHASED" : "PURCHASE", () -> {
                if (cosmetic.optBoolean("purchased")) {
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("You have already purchased " + cosmetic.optString("name") + ".");
                    return;
                }

                Hyperium.LOGGER.info("Attempting to purchase {}", key);
                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase", true).put("value", key)));
                }
            }));

            purchaseTab.addChild(purchase);
        });

        components.addAll(Arrays.asList(
            infoTab,
            purchaseTab
        ));
    }
}
