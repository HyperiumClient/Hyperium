package cc.hyperium.mods.oldanimations;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

class AnimationEventHandler {
    private final Minecraft mc;

    AnimationEventHandler() {
        this.mc = Minecraft.getMinecraft();
    }

    @InvokeEvent
    public void onRenderFirstHand(final RenderEvent e) {
        if (this.mc.thePlayer.getHeldItem() == null) {
            return;
        }
        this.attemptSwing();
    }

    private void attemptSwing() {
        if (this.mc.thePlayer.getItemInUseCount() > 0) { //TODO: Config option for 1.7 swing animation
            final boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                this.swingItem(this.mc.thePlayer);
            }
        }
    }

    private void swingItem(final EntityPlayerSP entityplayersp) {
        final int swingAnimationEnd = entityplayersp.isPotionActive(Potion.digSpeed) ? (6 - (1 + entityplayersp.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (entityplayersp.isPotionActive(Potion.digSlowdown) ? (6 + (1 + entityplayersp.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!entityplayersp.isSwingInProgress || entityplayersp.swingProgressInt >= swingAnimationEnd / 2 || entityplayersp.swingProgressInt < 0) {
            entityplayersp.swingProgressInt = -1;
            entityplayersp.isSwingInProgress = true;
        }
    }
}
