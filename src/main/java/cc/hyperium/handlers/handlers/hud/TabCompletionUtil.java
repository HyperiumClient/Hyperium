package cc.hyperium.handlers.handlers.hud;

import com.google.common.base.Functions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompletionUtil {
    public static List<String> getListOfStringsMatchingLastWord(final String[] p_175762_0_, final Collection<?> p_175762_1_) {
        final String s = p_175762_0_[p_175762_0_.length - 1];
        final List<String> list = new ArrayList<>();
        if (!p_175762_1_.isEmpty()) {
            for (final String s2 : p_175762_1_.stream().map(Functions.toStringFunction()::apply).collect(Collectors.toList())) {
                if (doesStringStartWith(s, s2)) {
                    list.add(s2);
                }
            }
            if (list.isEmpty()) {
                for (final Object object : p_175762_1_) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getResourcePath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }

    public static List<String> getListOfStringsMatchingLastWord(final String[] p_175762_0_, final String[] p_175762_1_) {
        return getListOfStringsMatchingLastWord(p_175762_0_, Arrays.asList(p_175762_1_));
    }

    private static boolean doesStringStartWith(final String original, final String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    public static List<String> getTabUsernames() {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final List<String> playerNames = new ArrayList<>();
        if (player == null) {
            return playerNames;
        }
        return player.sendQueue.getPlayerInfoMap().stream().map(netPlayerInfo -> netPlayerInfo.getGameProfile().getName()).collect(Collectors.toList());
    }

    public static List<EntityPlayer> getLoadedPlayers() {
        return Minecraft.getMinecraft().theWorld.playerEntities;
    }
}

