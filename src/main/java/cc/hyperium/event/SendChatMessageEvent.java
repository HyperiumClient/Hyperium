package cc.hyperium.event;

/**
 * Invoked when the player presses the enter key in the GuiChat class
 */
public class SendChatMessageEvent extends CancellableEvent {

    private final String message;

    /**
     * @param message the message the player is sending
     */
    public SendChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
