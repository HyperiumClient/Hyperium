package cc.hyperium.cosmetics.backpack;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public abstract class AbstractBackpackPet extends AbstractCosmetic {

    private float scale;

    public AbstractBackpackPet(EnumPurchaseType purchaseType) {
        super(false, purchaseType);
    }


    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        if (!isPurchasedBy(event.getEntity().getUniqueID()))
            return;
        scale = .1F;
        AbstractClientPlayer player = event.getEntity();
        Entity entity = getEntity(event.getPartialTicks(), player, event.getRenderManager(), event.getX(), event.getY(), event.getZ());
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        //Manage pos here;
        entity.posX = 0;
        entity.posY = 0;
        entity.posZ = 0;
        entity.lastTickPosX = 0;
        entity.lastTickPosY = 0;
        entity.lastTickPosZ = 0;
        float partialTicks = event.getPartialTicks();

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        GlStateManager.pushMatrix();

        double yaw = player.rotationYaw + (player.rotationYaw - player.prevRotationYaw) * (double) partialTicks;

        GlStateManager.translate(MathHelper.cos((float) Math.toRadians(yaw)), 1, MathHelper.sin((float) Math.toRadians(yaw)));
        GlStateManager.translate(d0 * scale, d1 * scale, d2 * scale);
        GlStateManager.scale(scale, scale, scale);
//        GlStateManager.rotate(90,1,0,0);
//        renderManager.renderEntitySimple(entity, event.getPartialTicks());
        GlStateManager.popMatrix();
        //render
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        tick();
    }

    public void tick() {

    }

    abstract Entity getEntity(float partialTicks, EntityPlayer player, RenderManager renderManager, double x, double y, double z);
}
