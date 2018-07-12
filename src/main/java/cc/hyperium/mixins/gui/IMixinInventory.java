package cc.hyperium.mixins.gui;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryEffectRenderer.class)
public interface IMixinInventory {
    @Accessor
    void setHasActivePotionEffects(boolean hasActivePotionEffects);
}
