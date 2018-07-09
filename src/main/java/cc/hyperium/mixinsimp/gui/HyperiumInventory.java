package cc.hyperium.mixinsimp.gui;

import cc.hyperium.mixins.gui.IMixinGuiContainer;
import cc.hyperium.mixins.gui.IMixinInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class HyperiumInventory {
    private InventoryEffectRenderer parent;

    public HyperiumInventory(InventoryEffectRenderer parent) {
        this.parent = parent;
    }

    public void updateActivePotionEffects(int xSize) {
        ((IMixinInventory) parent).setHasActivePotionEffects(!Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty());
        ((IMixinGuiContainer) parent).setGuiLeft((parent.width - xSize) / 2);
    }
}
