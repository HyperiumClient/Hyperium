package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.animation.AbstractAnimationHandler;
import cc.hyperium.handlers.handlers.animation.ArmWaveHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumKeybind;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class ArmWaveKeybind extends HyperiumKeybind {

  public ArmWaveKeybind() {
    super("Arm Wave", Keyboard.KEY_NONE, KeyType.COSMETIC);
  }

  private boolean armWaveToggled;

  @Override
  public void onPress() {
    ArmWaveHandler armWaveHandler = Hyperium.INSTANCE.getHandlers().getArmWaveHandler();
    UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
    AbstractAnimationHandler.AnimationState currentState = armWaveHandler.get(uuid);

    armWaveToggled = !Settings.ARM_WAVE_TOGGLE_MODE || !armWaveToggled;

    NettyClient client = NettyClient.getClient();

    if (Settings.ARM_WAVE_TOGGLE_MODE) {
      currentState.setToggled(armWaveToggled);
      if (armWaveToggled) {
        armWaveHandler.startAnimation(uuid);
      } else {
        armWaveHandler.stopAnimation(uuid);
      }

      if (client != null) {
        client.write(ServerCrossDataPacket
            .build(new JsonHolder().put("type", "armwave_update").put("posing", armWaveToggled)));
      }

      return;
    }

    if (Settings.ARM_WAVE_TOGGLE && currentState.isAnimating() && !wasPressed()) {
      currentState.setToggled(false);
      armWaveHandler.stopAnimation(uuid);

      if (client != null) {
        client.write(ServerCrossDataPacket
            .build(new JsonHolder().put("type", "armwave_update").put("posing", false)));
      }

      return;
    }

    if (!wasPressed()) {
      currentState.setToggled(Settings.ARM_WAVE_TOGGLE);
      armWaveHandler.startAnimation(uuid);
    }

    if (client != null) {
      client.write(ServerCrossDataPacket
          .build(new JsonHolder().put("type", "armwave_update").put("posing", true)));
    }
  }

  @Override
  public void onRelease() {
    if (!Settings.ARM_WAVE_TOGGLE_MODE) {
      armWaveToggled = false;
    }
    if (Settings.ARM_WAVE_TOGGLE || Settings.ARM_WAVE_TOGGLE_MODE) {
      return;
    }

    Hyperium.INSTANCE.getHandlers().getArmWaveHandler().stopAnimation(UUIDUtil.getClientUUID());
    NettyClient client = NettyClient.getClient();
    if (client != null) {
      client.write(ServerCrossDataPacket
          .build(new JsonHolder().put("type", "armwave_update").put("posing", false)));
    }
  }
}
