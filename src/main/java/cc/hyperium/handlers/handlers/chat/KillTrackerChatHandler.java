package cc.hyperium.handlers.handlers.chat;

import net.minecraft.util.IChatComponent;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class KillTrackerChatHandler extends HyperiumChatHandler {

    public KillTrackerChatHandler() {
    }

    /**
     * @param component Entire component from event
     * @param text      Pure text for parsing
     * @return
     */
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        return false;
    }
}
