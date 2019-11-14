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

package cc.hyperium.mods.chromahud;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.chromahud.api.ButtonConfig;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.api.StringConfig;
import cc.hyperium.mods.chromahud.api.TextConfig;
import cc.hyperium.mods.chromahud.commands.CommandChromaHUD;
import cc.hyperium.mods.chromahud.displayitems.chromahud.*;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ToggleSprintStatus;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChromaHUD extends AbstractMod {

    private static final String MODID = "ChromaHUD";
    public static final String VERSION = "3.0";
    private final Metadata meta;
    private File suggestedConfigurationFile;

    public ChromaHUD() {
        meta = new Metadata(this, MODID, VERSION, "Sk1er");
    }

    public AbstractMod init() {
        suggestedConfigurationFile = new File(Hyperium.folder, "/displayconfig.json");
        ChromaHUDApi.getInstance().register(new DefaultChromaHUDParser());
        ChromaHUDApi.getInstance().register(new HyperiumChromaHudParser());

        registerConfigElements();
        setup();

        EventBus.INSTANCE.register(new ElementRenderer(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandChromaHUD(this));
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    public void setup() {
        JsonHolder data = new JsonHolder();
        try {
            if (!suggestedConfigurationFile.exists()) {
                if (!suggestedConfigurationFile.getParentFile().exists()) {
                    suggestedConfigurationFile.getParentFile().mkdirs();
                }

                saveState();
            }

            FileReader fr = new FileReader(suggestedConfigurationFile);
            BufferedReader br = new BufferedReader(fr);
            data = new JsonHolder(br.lines().collect(Collectors.joining()));
            fr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChromaHUDApi.getInstance().post(data);
    }

    public List<DisplayElement> getDisplayElements() {
        return ChromaHUDApi.getInstance().getElements();
    }

    public GeneralConfigGui getConfigGuiInstance() {
        return new GeneralConfigGui(this);
    }

    /*
    Saves current state of all elements to file
     */
    public void saveState() {
        JsonHolder master = new JsonHolder();
        boolean enabled = true;
        master.put("enabled", enabled);
        JsonArray elementArray = new JsonArray();
        master.putArray("elements", elementArray);
        getDisplayElements().forEach(element -> {
            JsonHolder tmp = element.getData();
            JsonArray items = new JsonArray();
            for (DisplayItem item : element.getDisplayItems()) {
                JsonHolder raw = item.getData();
                raw.put("type", item.getType());
                items.add(raw.getObject());
            }
            elementArray.add(tmp.getObject());
            tmp.putArray("items", items);
        });

        try {
            if (!suggestedConfigurationFile.exists()) suggestedConfigurationFile.createNewFile();
            FileWriter fw = new FileWriter(suggestedConfigurationFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(master.toString());
            bw.close();
            fw.close();
        } catch (Exception ignored) {
        }
    }

    private void registerConfigElements() {
        ChromaHUDApi.getInstance().registerButtonConfig("COORDS", new ButtonConfig((guiButton, displayItem) -> {
            CordsDisplay displayItem1 = (CordsDisplay) displayItem;
            displayItem1.state = displayItem1.state == 1 ? 0 : 1;
            guiButton.displayString = ChatColor.RED.toString() + "Make " + (((CordsDisplay) displayItem).state == 1 ? "Horizontal" : "Vertical");
        }, new GuiButton(0, 0, 0, "Coords State"), (guiButton, displayItem) -> guiButton.displayString = ChatColor.RED.toString() + "Make "
            + (((CordsDisplay) displayItem).state == 1 ? "Horizontal" : "Vertical")));
        ChromaHUDApi.getInstance().registerButtonConfig("COORDS", new ButtonConfig((guiButton, displayItem) -> {
            CordsDisplay displayItem1 = (CordsDisplay) displayItem;
            displayItem1.precision += 1;
            if (displayItem1.precision > 4)
                displayItem1.precision = 0;
            int next = displayItem1.precision + 1;
            if (next > 4)
                next = 0;
            guiButton.displayString = ChatColor.RED.toString() + "Change to " + next + " decimal" + (next != 1 ? "s" : "");
        }, new GuiButton(0, 0, 0, "Coords Precision"), (guiButton, displayItem) -> {
            int next = ((CordsDisplay) displayItem).precision + 1;
            if (next > 4)
                next = 0;
            guiButton.displayString = ChatColor.RED.toString() + "Change to " + next + " decimal" + (next != 1 ? "s" : "");
        }));

        ChromaHUDApi.getInstance().registerButtonConfig("POTION", new ButtonConfig((guiButton, displayItem) -> {
            PotionEffects potionEffects = (PotionEffects) displayItem;
            potionEffects.togglePotionIcon();
            guiButton.displayString = ChatColor.RED + "Toggle Potion Icon";
        }, new GuiButton(0, 0, 0, "Potion Icons"), (guiButton, displayItem) -> guiButton.displayString = ChatColor.RED.toString() + "Toggle Potion Icon"));

        ChromaHUDApi.getInstance().registerButtonConfig("DIRECTION", new ButtonConfig((guiButton, displayItem) -> {
            DirectionHUD directionHUD = (DirectionHUD) displayItem;
            directionHUD.toggleShortDirection();
            guiButton.displayString = ChatColor.RED + "Toggle Short Direction";
        }, new GuiButton(0, 0, 0, "Short Directions"), ((guiButton, displayItem) -> guiButton.displayString = ChatColor.RED + "Toggle Short Direction")));

        ChromaHUDApi.getInstance().registerButtonConfig("ARMOUR_HUD", new ButtonConfig((guiButton, displayItem) -> {
            ArmourHud item = (ArmourHud) displayItem;
            item.toggleDurability();
            guiButton.displayString = ChatColor.RED.toString() + "Toggle Durability";
        }, new GuiButton(0, 0, 0, "Armour Hud Durability"), (guiButton, displayItem) -> guiButton.displayString = ChatColor.RED.toString() + "Toggle Durability"));

        ChromaHUDApi.getInstance().registerButtonConfig("ARMOUR_HUD", new ButtonConfig((guiButton, displayItem) -> {
            ArmourHud item = (ArmourHud) displayItem;
            item.toggleHand();
            guiButton.displayString = ChatColor.RED.toString() + "Toggle Held Item";
        }, new GuiButton(0, 0, 0, "Armour Hud Hand"), (guiButton, displayItem) -> guiButton.displayString = ChatColor.RED.toString() + "Toggle Held Item"));

        ChromaHUDApi.getInstance().registerButtonConfig("ARMOUR_HUD", new ButtonConfig((guiButton, displayItem) -> {
            ArmourHud item = (ArmourHud) displayItem;
            item.setArmourOnTop(!item.isArmourOnTop());
            guiButton.displayString = ChatColor.RED.toString() + "Toggle Armour On Top";
        }, new GuiButton(0, 0, 0, "Armour Hud Hand"), (guiButton, displayItem) -> guiButton.displayString = ChatColor.RED.toString() + "Toggle Armour On Top"));

        GuiTextField textTextField = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, 0, 0, 200, 20);
        ChromaHUDApi.getInstance().registerTextConfig("TEXT", new TextConfig((guiTextField, displayItem) -> ((TextItem) displayItem).setText(guiTextField.getText()),
            textTextField, (guiTextField, displayItem) -> guiTextField.setText(((TextItem) displayItem).getText())));

        ChromaHUDApi.getInstance().registerTextConfig("TIME", new TextConfig((guiTextField, displayItem) -> ((TimeHud) displayItem).setFormat(guiTextField.getText()),
            textTextField, (guiTextField, displayItem) -> guiTextField.setText(((TimeHud) displayItem).getFormat())));
        ChromaHUDApi.getInstance().registerStringConfig("TIME", new StringConfig("Accepted Formats\n" +
            "YY - Year\n" +
            "MM - Month\n" +
            "dd - Day\n" +
            "HH - Hour\n" +
            "mm - Minute\n" +
            "ss - Second\n" +
            "For more options, Google \"Date Format\""));

        ChromaHUDApi.getInstance().registerButtonConfig("SCOREBOARD", new ButtonConfig((guiButton, displayItem) -> displayItem.getData().put("numbers",
            !displayItem.getData().optBoolean("numbers")), new GuiButton(0, 0, 0, "Toggle Number"), (guiButton, displayItem) -> {
        }));

        ChromaHUDApi.getInstance().registerTextConfig("SPRINT_STATUS", new TextConfig((guiTextField, displayItem) -> ((ToggleSprintStatus) displayItem)
            .setSprintEnabledText(guiTextField.getText()), textTextField, (guiTextField, displayItem) -> guiTextField.setText(((ToggleSprintStatus) displayItem).getStatusText())));

        ChromaHUDApi.getInstance().registerButtonConfig("COINS", new ButtonConfig((guiButton, displayItem) -> {
            JsonHolder data = displayItem.getData();
            int state = data.optInt("state");
            state++;
            if (state < 0 || state > 2) {
                state = 0;
            }
            data.put("state", state);

        }, new GuiButton(0, 0, 0, "Toggle Number"), (guiButton, displayItem) -> {
            JsonHolder data = displayItem.getData();
            int state = data.optInt("state");
            if (state < 0 || state > 2) state = 0;
            if (state == 0) guiButton.displayString = "Daily Coins";
            if (state == 1) guiButton.displayString = "Monthly Coins";
            if (state == 2) guiButton.displayString = "Lifetime Coins";
        }));
    }
}
