package cc.hyperium.event;

import net.minecraft.util.IChatComponent;

/**
 * Invoked once a chat message is sent
 */
public class ChatEvent extends CancellableEvent {

    private final IChatComponent chat;

    public ChatEvent(IChatComponent chat) {
        this.chat = chat;
    }

    public IChatComponent getChat() {
        return this.chat;
    }
}
