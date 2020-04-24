package cc.hyperium.hooks;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.entity.FovUpdateEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;

public class AbstractClientPlayerHook {

  public static float getFovModifierHook(AbstractClientPlayer player) {
    float currentFov = 1.0F;

    if (player.capabilities.isFlying) {
      currentFov *= 1.1F;
    }

    IAttributeInstance attributes = player
        .getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    currentFov = (float) ((double) currentFov * (
        (attributes.getAttributeValue() / (double) player.capabilities.getWalkSpeed() + 1.0D)
            / 2.0D));

    if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(currentFov) || Float
        .isInfinite(currentFov)) {
      currentFov = 1.0F;
    }

    if (player.isUsingItem() && player.getItemInUse().getItem() == Items.bow) {
      int itemUseDuration = player.getItemInUseDuration();
      float tickDuration = (float) itemUseDuration / 20.0F;

      if (tickDuration > 1.0F) {
        tickDuration = 1.0F;
      } else {
        tickDuration = tickDuration * tickDuration;
      }

      currentFov *= 1.0F - tickDuration * 0.15F;
    }

    // Hyperium
    FovUpdateEvent event = new FovUpdateEvent(player, currentFov);
    EventBus.INSTANCE.post(event);
    return event.getNewFov();
  }

}
