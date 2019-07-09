package cc.hyperium.mixinsimp.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class HyperiumGuiScreenResourcePacks {

    private GuiScreenResourcePacks parent;

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

    public void drawScreen(GuiResourcePackAvailable availableResourcePacksList, GuiResourcePackSelected selectedResourcePacksList, int mouseX, int mouseY, float partialTicks, FontRenderer fontRendererObj, int width) {
        parent.drawBackground(0);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2,
            16, 16777215);
    }
}
