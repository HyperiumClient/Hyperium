package cc.hyperium.mixinsimp.gui;

import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.universal.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class HyperiumGuiScreenResourcePacks {
    private GuiScreenResourcePacks parent;

    private GuiResourcePackAvailable availableResourcePacksList;

    public HyperiumGuiScreenResourcePacks(GuiScreenResourcePacks parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            b.setWidth(200);
            if (b.id == 2) {
                b.xPosition = parent.width / 2 - 204;
            }
        });
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public GuiResourcePackAvailable updateList(GuiTextField searchField, GuiResourcePackAvailable availablePacksClone,
                                               List<ResourcePackListEntry> availableResourcePacks, Minecraft mc, int height, int width) {
        if (searchField == null || searchField.getText().isEmpty()) {
            this.availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height,
                availableResourcePacks);
            this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
            this.availableResourcePacksList.registerScrollButtons(7, 8);
        } else {
            this.availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height,
                Arrays
                    .asList(availablePacksClone.getList().stream().filter(resourcePackListEntry -> {
                        try {
                            Method nameMethod = ReflectionUtil
                                .findMethod(resourcePackListEntry.getClass(),
                                    new String[]{"func_148312_b", "c"});
                            nameMethod.setAccessible(true);
                            String name = ChatColor
                                .stripColor((String) nameMethod.invoke(resourcePackListEntry))
                                .replaceAll("[^A-Za-z0-9 ]", "").trim().toLowerCase();
                            String text = searchField.getText().toLowerCase();

                            if (name.endsWith("zip")) {
                                name = name.subSequence(0, name.length() - 3).toString();
                            }

                            for (String s : text.split(" ")) {
                                if (!name.contains(s.toLowerCase())) {
                                    return false;
                                }
                            }

                            return name.startsWith(text) || name.contains(text) || name
                                .equalsIgnoreCase(text);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            return true;
                        }
                    }).toArray(ResourcePackListEntry[]::new)));
            this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
            this.availableResourcePacksList.registerScrollButtons(7, 8);
        }

        return availableResourcePacksList;
    }

    public void drawScreen(GuiResourcePackAvailable availableResourcePacksList, GuiResourcePackSelected selectedResourcePacksList, int mouseX, int mouseY
        , float partialTicks, FontRenderer fontRendererObj, GuiTextField searchField, int width) {
        parent.drawBackground(0);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2,
            16, 16777215);

        searchField.drawTextBox();
    }

}
