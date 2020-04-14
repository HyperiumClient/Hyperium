package cc.hyperium.hooks;

import cc.hyperium.Metadata;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.network.LoginReplyHandler;
import com.google.common.base.Charsets;
import io.netty.buffer.Unpooled;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class NetHandlerPlayClientHook {

  public static void readAddonData(NetHandlerPlayClient client, S3FPacketCustomPayload packetIn) {
    PacketBuffer packetBuffer = packetIn.getBufferData();
    try {
      int readableBytes = packetBuffer.readableBytes();

      if (readableBytes > 0) {
        byte[] payload = new byte[readableBytes - 1];
        packetBuffer.readBytes(payload);
        String message = new String(payload, Charsets.UTF_8);

        if (LoginReplyHandler.SHOW_MESSAGES) {
          GeneralChatHandler.instance().sendMessage(
              "Packet message on channel " + packetIn.getChannelName() + " -> " + message);
        }

        if ("REGISTER".equalsIgnoreCase(packetIn.getChannelName())) {
          if (message.contains("Hyperium")) {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeString("Hyperium;" + Metadata.getVersion() + ";" + Metadata.getVersionID());
            client.addToSendQueue(new C17PacketCustomPayload("REGISTER", buffer));
            PacketBuffer addonbuffer = new PacketBuffer(Unpooled.buffer());
            List<AddonManifest> addons = AddonBootstrap.INSTANCE.getAddonManifests();
            addonbuffer.writeInt(addons.size());

            for (AddonManifest addonmanifest : addons) {
              String addonName = addonmanifest.getName();
              String version = addonmanifest.getVersion();

              if (addonName == null) {
                addonName = addonmanifest.getMainClass();
              }

              if (version == null) {
                version = "unknown";
              }

              addonbuffer.writeString(addonName);
              addonbuffer.writeString(version);
            }

            client.addToSendQueue(new C17PacketCustomPayload("hyperium|Addons", addonbuffer));
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static boolean validateResourcePackUrl(NetHandlerPlayClient client, String url,
      String hash) {
    try {
      URI uri = new URI(url);
      String scheme = uri.getScheme();
      boolean isLevelProtocol = "level".equals(scheme);

      if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
        client.getNetworkManager().sendPacket(new C19PacketResourcePackStatus(hash,
            C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
        throw new URISyntaxException(url, "Wrong protocol");
      }
      url = URLDecoder
          .decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());

      if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
        System.out.println("Malicious server tried to access " + url);
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer != null) {
          thePlayer.addChatMessage(new ChatComponentText(
              EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString()
                  + "[WARNING] The current server has attempted to be malicious but we have stopped them."));
        }
        throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
      }

      return true;
    } catch (URISyntaxException e) {

      return false;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return false;
  }
}
