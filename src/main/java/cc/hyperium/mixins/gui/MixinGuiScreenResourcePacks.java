package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiResourcePack;
import cc.hyperium.mixinsimp.gui.HyperiumGuiScreenResourcePacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

    @Shadow private GuiResourcePackAvailable availableResourcePacksList;
    @Shadow private GuiResourcePackSelected selectedResourcePacksList;
    private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks((GuiScreenResourcePacks) (Object) this);

    /**
     * @reason Change buttons size
     *
     * @author SiroQ
     */
    @Inject(method = "initGui",at = @At("RETURN"))
    public void initGui(CallbackInfo callbackInfo) {
        hyperiumGuiResourcePack.initGui(this.buttonList);
    }
    /**
     * @reason Change text location
     *
     * @author SiroQ
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo"), this.width / 2 - 102, this.height - 26, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
