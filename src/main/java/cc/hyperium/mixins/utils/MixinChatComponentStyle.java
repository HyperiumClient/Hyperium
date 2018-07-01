package cc.hyperium.mixins.utils;

import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChatComponentStyle.class)
public abstract class MixinChatComponentStyle implements IChatComponent {
    @Shadow
    protected List<IChatComponent> siblings;
    @Shadow
    private ChatStyle style;
    private String cachedBuilt;

    @Shadow
    public abstract ChatStyle getChatStyle();

    @Overwrite
    public IChatComponent setChatStyle(ChatStyle style) {
        this.style = style;
        this.cachedBuilt = null;
        for (IChatComponent ichatcomponent : this.siblings) {
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }

        return this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public final String getFormattedText() {
        if (cachedBuilt != null)
            return cachedBuilt;

        StringBuilder stringbuilder = new StringBuilder();

        for (IChatComponent ichatcomponent : this) {
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append((Object) EnumChatFormatting.RESET);
        }

        String s = stringbuilder.toString();
        this.cachedBuilt = s;
        return s;
    }


}
