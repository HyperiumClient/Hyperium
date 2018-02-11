package com.hcc.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventory extends GuiContainer {

    @Shadow
    private boolean hasActivePotionEffects;

    public MixinInventory(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    /**
     * Removes the inventory going to the left once potion effects have worn out
     *
     * @author Kevin
     */
    @Overwrite
    protected void updateActivePotionEffects() {
        this.hasActivePotionEffects = !Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty();
        this.guiLeft = (this.width - this.xSize) / 2;
    }

}