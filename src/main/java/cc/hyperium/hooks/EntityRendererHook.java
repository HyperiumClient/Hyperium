package cc.hyperium.hooks;

import cc.hyperium.Hyperium;
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class EntityRendererHook {

  public static void updateRendererHook(Minecraft mc) {
    if (Hyperium.INSTANCE.getHandlers() == null || Hyperium.INSTANCE.getHandlers().getPerspectiveHandler() == null) {
      return;
    }

    PerspectiveModifierHandler perspectiveHandler = Hyperium.INSTANCE.getHandlers().getPerspectiveHandler();
    boolean activeDisplay = Display.isActive();

    if (mc.inGameHasFocus && activeDisplay) {
      if (perspectiveHandler.enabled && mc.gameSettings.thirdPersonView != 1) {
        perspectiveHandler.onDisable();
      }

      if (perspectiveHandler.enabled) {
        mc.mouseHelper.mouseXYChange();

        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;
        float f2 = (float) mc.mouseHelper.deltaX * f1;
        float f3 = (float) mc.mouseHelper.deltaY * f1;

        // Modifying pitch and yaw values.
        perspectiveHandler.modifiedYaw += f2 / 8.0F;
        perspectiveHandler.modifiedPitch += f3 / 8.0F;

        // Checking if pitch exceeds maximum range.
        if (Math.abs(perspectiveHandler.modifiedPitch) > 90.0F) {
          if (perspectiveHandler.modifiedPitch > 0.0F) {
            perspectiveHandler.modifiedPitch = 90.0F;
          } else {
            perspectiveHandler.modifiedPitch = -90.0F;
          }
        }
      }
    }
  }
}
