package cc.hyperium.hooks;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.entity.FovUpdateEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;

public class AbstractClientPlayerHook {

  public static float getFovModifierHook(AbstractClientPlayer player) {
    float f = 1.0F;

    if (player.capabilities.isFlying) {
      f *= 1.1F;
    }

    IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

    if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
      f = 1.0F;
    }

    if (player.isUsingItem() && player.getItemInUse().getItem() == Items.bow) {
      int i = player.getItemInUseDuration();
      float f1 = (float)i / 20.0F;

      if (f1 > 1.0F) {
        f1 = 1.0F;
      } else {
        f1 = f1 * f1;
      }

      f *= 1.0F - f1 * 0.15F;
    }

    // Hyperium
    FovUpdateEvent event = new FovUpdateEvent(player, f);
    EventBus.INSTANCE.post(event);
    return event.getNewFov();
  }

}
