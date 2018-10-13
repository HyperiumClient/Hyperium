package cc.hyperium.cosmetics.backpack;

import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;

public class EnderDragonBackpack extends AbstractBackpackPet {

    EntityDragon entityDragon;

    public EnderDragonBackpack() {
        super(EnumPurchaseType.BACKPACK_ENDER_DRAGON);
    }

    @Override
    public void tick() {
        if (entityDragon != null)
            entityDragon.onLivingUpdate();
    }

    @Override
    Entity getEntity(float partialTicks, EntityPlayer player, RenderManager renderManager, double x, double y, double z) {
        if (entityDragon == null) {
            entityDragon = new EntityDragon(player.getEntityWorld());
        }
        entityDragon.setWorld(player.getEntityWorld());
        entityDragon.ticksExisted = 1;
        entityDragon.rotationPitch = player.rotationPitch;
        entityDragon.prevRotationPitch = player.prevRotationPitch;

        entityDragon.rotationYaw = player.rotationYaw+180;
        entityDragon.prevRotationYaw = player.prevRotationYaw+180;

        entityDragon.renderYawOffset = player.renderYawOffset;
        entityDragon.prevRenderYawOffset = player.prevRenderYawOffset;

        entityDragon.rotationYawHead = player.rotationYawHead+180;
        entityDragon.prevRotationYawHead = player.prevRotationYawHead+180;


        entityDragon.limbSwing = player.limbSwing;
        entityDragon.limbSwingAmount = player.limbSwingAmount;
        entityDragon.prevLimbSwingAmount = player.prevLimbSwingAmount;

        return entityDragon;
    }
}
