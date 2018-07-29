package cc.hyperium.addons.sidebar.versions;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtils {
    private final IChatComponent parent;
    private String text;
    private ChatStyle style;

    private ChatUtils(final String text) {
        this(text, null, Inheritance.SHALLOW);
    }

    private ChatUtils(final String text, final IChatComponent parent, final Inheritance inheritance) {
        this.parent = parent;
        this.text = text;
        switch (inheritance) {
            case DEEP: {
                this.style = ((parent != null) ? parent.getChatStyle() : new ChatStyle());
                break;
            }
            default: {
                this.style = new ChatStyle();
                break;
            }
            case NONE: {
                this.style = new ChatStyle().setColor((EnumChatFormatting) null).setBold(false).setItalic(false).setStrikethrough(false).setUnderlined(false).setObfuscated(false).setChatClickEvent((ClickEvent) null).setChatHoverEvent((HoverEvent) null).setInsertion((String) null);
                break;
            }
        }
    }

    public static ChatUtils of(final String text) {
        return new ChatUtils(text);
    }

    public ChatUtils setColor(final EnumChatFormatting color) {
        this.style.setColor(color);
        return this;
    }

    public ChatUtils setBold(final boolean bold) {
        this.style.setBold(bold);
        return this;
    }

    public ChatUtils setItalic(final boolean italic) {
        this.style.setItalic(italic);
        return this;
    }

    public ChatUtils setStrikethrough(final boolean strikethrough) {
        this.style.setStrikethrough(strikethrough);
        return this;
    }

    public ChatUtils setUnderlined(final boolean underlined) {
        this.style.setUnderlined(underlined);
        return this;
    }

    public ChatUtils setObfuscated(final boolean obfuscated) {
        this.style.setObfuscated(obfuscated);
        return this;
    }

    public ChatUtils setClickEvent(final ClickEvent.Action action, final String value) {
        this.style.setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public ChatUtils setHoverEvent(final String value) {
        return this.setHoverEvent((IChatComponent) new ChatComponentText(value));
    }

    public ChatUtils setHoverEvent(final IChatComponent value) {
        return this.setHoverEvent(HoverEvent.Action.SHOW_TEXT, value);
    }

    public ChatUtils setHoverEvent(final HoverEvent.Action action, final IChatComponent value) {
        this.style.setChatHoverEvent(new HoverEvent(action, value));
        return this;
    }

    public ChatUtils setInsertion(final String insertion) {
        this.style.setInsertion(insertion);
        return this;
    }

    public ChatUtils append(final String text) {
        return this.append(text, Inheritance.SHALLOW);
    }

    public ChatUtils append(final String text, final Inheritance inheritance) {
        return new ChatUtils(text, this.build(), inheritance);
    }

    public IChatComponent build() {
        final IChatComponent thisComponent = new ChatComponentText(this.text).setChatStyle(this.style);
        return (this.parent != null) ? this.parent.appendSibling(thisComponent) : thisComponent;
    }

    public enum Inheritance {
        DEEP,
        SHALLOW,
        NONE;
    }
}
