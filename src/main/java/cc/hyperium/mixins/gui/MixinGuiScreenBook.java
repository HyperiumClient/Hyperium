package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiScreenBook.class)
public interface MixinGuiScreenBook {
    @Accessor
    NBTTagList getBookPages();

    @Accessor
    int getCurrPage();

    @Accessor
    void setBookPages(NBTTagList tagList);

    @Accessor
    void setCurrPage(int page);
}
