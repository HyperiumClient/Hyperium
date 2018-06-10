package cc.hyperium.utils;

import net.minecraft.client.Minecraft;

import java.util.UUID;

/**
 * Created by mitchellkatz on 5/2/18. Designed for production use on Sk1er.club
 */
public class UUIDUtil {

    public static String getUUIDWithoutDashes() {
        return getClientUUID().toString().toLowerCase().replace("-", "");
    }

    public static UUID getClientUUID() {
        return Minecraft.getMinecraft().getSession().getProfile().getId();
    }
}
