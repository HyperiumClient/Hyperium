package cc.hyperium.mixins.utils;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChatStyle.class)
public abstract class MixinChatStyle {

    @Shadow
    private ChatStyle parentStyle;
    private String cachedState;
    @Shadow
    private EnumChatFormatting color;
    @Shadow
    private Boolean bold;
    @Shadow
    private Boolean italic;
    @Shadow
    private Boolean strikethrough;
    @Shadow
    private Boolean underlined;
    @Shadow
    private Boolean obfuscated;
    @Shadow
    private ClickEvent chatClickEvent;
    @Shadow
    private HoverEvent chatHoverEvent;
    @Shadow
    private String insertion;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract EnumChatFormatting getColor();

    @Shadow
    public abstract boolean getBold();

    @Shadow
    public abstract boolean getItalic();

    @Shadow
    public abstract boolean getUnderlined();

    @Shadow
    public abstract boolean getObfuscated();

    @Shadow
    public abstract boolean getStrikethrough();

    /**
     * @author Sk1er
     */
    @Overwrite
    public String getFormattingCode() {
        if (cachedState != null)
            return cachedState;
        if (this.isEmpty()) {
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            if (this.getColor() != null) {
                stringbuilder.append((Object) this.getColor());
            }

            if (this.getBold()) {
                stringbuilder.append((Object) EnumChatFormatting.BOLD);
            }

            if (this.getItalic()) {
                stringbuilder.append((Object) EnumChatFormatting.ITALIC);
            }

            if (this.getUnderlined()) {
                stringbuilder.append((Object) EnumChatFormatting.UNDERLINE);
            }

            if (this.getObfuscated()) {
                stringbuilder.append((Object) EnumChatFormatting.OBFUSCATED);
            }

            if (this.getStrikethrough()) {
                stringbuilder.append((Object) EnumChatFormatting.STRIKETHROUGH);
            }

            String s = stringbuilder.toString();
            cachedState = s;
            return s;
        }
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setColor(EnumChatFormatting color) {
        this.color = color;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setBold(Boolean boldIn) {
        this.bold = boldIn;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setItalic(Boolean italic) {
        this.italic = italic;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setChatClickEvent(ClickEvent event) {
        this.chatClickEvent = event;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setChatHoverEvent(HoverEvent event) {
        this.chatHoverEvent = event;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setInsertion(String insertion) {
        this.insertion = insertion;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public ChatStyle setParentStyle(ChatStyle parent) {
        this.parentStyle = parent;
        this.cachedState = null;
        return (ChatStyle) (Object) this;
    }
}
