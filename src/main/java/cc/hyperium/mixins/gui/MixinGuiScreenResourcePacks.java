package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiScreenResourcePacks;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author KodingKing
 */
@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

    @Shadow
    private GuiResourcePackAvailable availableResourcePacksList;

    @Shadow
    private GuiResourcePackSelected selectedResourcePacksList;

    @Shadow
    private List<ResourcePackListEntry> availableResourcePacks;

    private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks(
        (GuiScreenResourcePacks) (Object) this);

    private GuiResourcePackAvailable availablePacksClone;

    private GuiTextField searchField;

    /**
     * @reason Change buttons size
     * @author SiroQ
     */
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo callbackInfo) {
        hyperiumGuiResourcePack.initGui(this.buttonList);

        this.availablePacksClone = this.availableResourcePacksList;
        this.searchField = new GuiTextField(3, fontRendererObj, this.width / 2 - 4 - 200,
            this.height - 24, 200, 20);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (this.searchField != null) {
            this.searchField.textboxKeyTyped(typedChar, keyCode);
        }

        updateList();
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) throws IOException {
        if (this.searchField != null) {
            this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        updateList();
    }

    /**
     * @reason Change text location
     * @author SiroQ
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hyperiumGuiResourcePack.drawScreen(availableResourcePacksList, selectedResourcePacksList, mouseX, mouseY, partialTicks, fontRendererObj, searchField, width);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateList() {
        availableResourcePacksList = hyperiumGuiResourcePack.updateList(searchField, availablePacksClone, availableResourcePacks, mc, height, width);
    }
}
