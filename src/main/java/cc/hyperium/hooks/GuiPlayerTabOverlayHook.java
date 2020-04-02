package cc.hyperium.hooks;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.StringUtil;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class GuiPlayerTabOverlayHook {

  private static final Minecraft mc = Minecraft.getMinecraft();

  public static int getNewColor(int color) {
    if (!Settings.CUSTOM_TAB_OPACITY) {
      return color;
    }

    int prevOpacity = Math.abs(color >> 24);
    int opacity = prevOpacity * Settings.TAB_OPACITY / 100;
    return (opacity << 24) | (color & 0xFFFFFF);
  }

  public static void renderDot(int j2, int k2, String s1, GameProfile gameprofile) {
    if (Settings.SHOW_ONLINE_PLAYERS) {
      int renderX = j2 + mc.fontRendererObj.getStringWidth(s1) + 2;
      String s = "⚫";

      boolean online =
          mc.getSession().getProfile().getId() == gameprofile.getId() || Hyperium.INSTANCE
              .getHandlers().getStatusHandler().isOnline(gameprofile.getId());

      if (StaffUtils.isStaff(gameprofile.getId()) || StaffUtils.isBooster(gameprofile.getId())) {
        StaffUtils.DotColour colour = StaffUtils.getColor(gameprofile.getId());

        if (colour.isChroma) {
          StringUtil.INSTANCE.drawChromaWaveString(s, renderX, (k2 - 2));
        } else {
          String format = StaffUtils.getColor(gameprofile.getId()).baseColour + s;
          mc.fontRendererObj.drawString(format, renderX, (k2 - 2), -1);
        }
      } else {
        String format =
            online ? ChatColor.GREEN + s : (Settings.OFFLINE_DOTS ? ChatColor.RED + s : "");
        mc.fontRendererObj.drawString(format, renderX, (k2 - 2), -1);
      }
    }
  }

  public static void moveHypixelFriends(List<NetworkPlayerInfo> list) {
    ConcurrentLinkedDeque<NetworkPlayerInfo> friends = new ConcurrentLinkedDeque<>();
    List<UUID> friendUUIDList = HypixelAPI.INSTANCE.getListOfCurrentUsersFriends();

    for (NetworkPlayerInfo networkPlayerInfo : list) {
      UUID id = networkPlayerInfo.getGameProfile().getId();

      if (friendUUIDList.contains(id)) {
        friends.add(networkPlayerInfo);
      }
    }

    list.removeAll(friends);
    friends.addAll(list);
    list.clear();
    list.addAll(friends);
  }
}
