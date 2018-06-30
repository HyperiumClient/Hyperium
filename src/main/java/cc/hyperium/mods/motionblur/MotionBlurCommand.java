package cc.hyperium.mods.motionblur;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.config.Settings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.math.NumberUtils;

public class MotionBlurCommand implements BaseCommand {

  private Minecraft mc;

  public MotionBlurCommand() {
    this.mc = Minecraft.getMinecraft();
  }

  public void onExecute(String[] args) {
    if (args.length != 1) {
      mc.thePlayer.addChatMessage(new ChatComponentText("Usage: /motionblur <0 - 7>."));
    } else {
      if (MotionBlurMod.isFastRenderEnabled()) {
        mc.thePlayer.addChatMessage(new ChatComponentText(
            "Motion blur does not work if Fast Render is enabled, please disable it in Options > Video Settings > Performance."));
        return;
      }

      int amount = NumberUtils.toInt(args[0], -1);

      if (amount < 0 || amount > 7) {
        mc.thePlayer.addChatMessage(new ChatComponentText("Invalid amount."));
        return;
      }

      if (amount != 0) {
        Settings.MOTION_BLUR_ENABLED = true;
        Settings.MOTION_BLUR_AMOUNT = (double) amount;

        try {
          MotionBlurMod.applyShader();
          mc.thePlayer.addChatMessage(new ChatComponentText("Motion blur enabled."));
        } catch (Throwable var5) {
          mc.thePlayer.addChatMessage(new ChatComponentText("Failed to enable Motion blur."));
          var5.printStackTrace();
        }
      } else {
        Settings.MOTION_BLUR_ENABLED = false;
        clearShaders();
      }
    }
  }

  public String getName() {
    return "motionblur";
  }

  public String getUsage() {
    return "/motionblur <0 - 7>";
  }

  public void clearShaders(){
    Method method = Reflection
        .getMethod(EntityRenderer.class, new String[]{"clearShaders"},null);
    try {
      method.invoke(Minecraft.getMinecraft().entityRenderer);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
