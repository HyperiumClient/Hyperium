package cc.hyperium.mixinsimp.entity;

import cc.hyperium.mixins.entity.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class HyperiumEntity {

    private Entity parent;
    private long nameCacheTime = System.currentTimeMillis();
    private IChatComponent cachedName;

    public HyperiumEntity(Entity parent) {
        this.parent = parent;
    }

    public IChatComponent getDisplayName() {
        if (cachedName == null || System.currentTimeMillis() - nameCacheTime > 50L) {
            ChatComponentText chatcomponenttext = new ChatComponentText(parent.getName());
            //not needed otherwise
            if (Minecraft.getMinecraft().isIntegratedServerRunning())
                chatcomponenttext.getChatStyle().setChatHoverEvent(((IMixinEntity) parent).callGetHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(parent.getUniqueID().toString());
            cachedName = chatcomponenttext;
            nameCacheTime = System.currentTimeMillis();
        }
        return cachedName;
    }
}
