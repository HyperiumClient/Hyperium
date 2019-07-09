package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiScreenResourcePacks;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KodingKing
 */
@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

    @Shadow private GuiResourcePackAvailable availableResourcePacksList;
    @Shadow private GuiResourcePackSelected selectedResourcePacksList;

    private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks(
        (GuiScreenResourcePacks) (Object) this);

    /**
     * @reason Change buttons size
     * @author SiroQ
     */
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo callbackInfo) {
        hyperiumGuiResourcePack.initGui(this.buttonList);
    }

    /**
     * @reason Change text location
     * @author SiroQ
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hyperiumGuiResourcePack.drawScreen(availableResourcePacksList, selectedResourcePacksList, mouseX, mouseY, partialTicks, fontRendererObj, width);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
