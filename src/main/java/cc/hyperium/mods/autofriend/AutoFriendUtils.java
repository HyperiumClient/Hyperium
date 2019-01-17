package cc.hyperium.mods.autofriend;

import net.minecraft.event.*;
import net.minecraft.util.*;

/*
credit to 2PI for help
*/
public class AutoFriendUtils {
    private final IChatComponent parent;
    private String text;
    private ChatStyle style;

    private AutoFriendUtils(final String text) {
        this(text, null, Inheritance.SHALLOW);
    }

    private AutoFriendUtils(final String text, final IChatComponent parent, final Inheritance inheritance) {
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
                this.style = new ChatStyle().setColor(null).setBold(false).setItalic(false).setStrikethrough(false).setUnderlined(false).setObfuscated(false).setChatClickEvent(null).setChatHoverEvent(null).setInsertion(null);
                break;
            }
        }
    }

    public static AutoFriendUtils of(final String text) {
        return new AutoFriendUtils(text);
    }

    public AutoFriendUtils setColor(final EnumChatFormatting color) {
        this.style.setColor(color);
        return this;
    }

    public AutoFriendUtils setBold(final boolean bold) {
        this.style.setBold(bold);
        return this;
    }

    public AutoFriendUtils setItalic(final boolean italic) {
        this.style.setItalic(italic);
        return this;
    }

    public AutoFriendUtils setStrikethrough(final boolean strikethrough) {
        this.style.setStrikethrough(strikethrough);
        return this;
    }

    public AutoFriendUtils setUnderlined(final boolean underlined) {
        this.style.setUnderlined(underlined);
        return this;
    }

    public AutoFriendUtils setObfuscated(final boolean obfuscated) {
        this.style.setObfuscated(obfuscated);
        return this;
    }

    public AutoFriendUtils setClickEvent(final ClickEvent.Action action, final String value) {
        this.style.setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public AutoFriendUtils setHoverEvent(final String value) {
        return this.setHoverEvent(new ChatComponentText(value));
    }

    public AutoFriendUtils setHoverEvent(final IChatComponent value) {
        return this.setHoverEvent(HoverEvent.Action.SHOW_TEXT, value);
    }

    public AutoFriendUtils setHoverEvent(final HoverEvent.Action action, final IChatComponent value) {
        this.style.setChatHoverEvent(new HoverEvent(action, value));
        return this;
    }

    public AutoFriendUtils setInsertion(final String insertion) {
        this.style.setInsertion(insertion);
        return this;
    }

    public AutoFriendUtils append(final String text) {
        return this.append(text, Inheritance.SHALLOW);
    }

    public AutoFriendUtils append(final String text, final Inheritance inheritance) {
        return new AutoFriendUtils(text, this.build(), inheritance);
    }

    public IChatComponent build() {
        final IChatComponent thisComponent = new ChatComponentText(this.text).setChatStyle(this.style);
        return (this.parent != null) ? this.parent.appendSibling(thisComponent) : thisComponent;
    }

    public enum Inheritance {
        DEEP,
        SHALLOW,
        NONE
    }
}
