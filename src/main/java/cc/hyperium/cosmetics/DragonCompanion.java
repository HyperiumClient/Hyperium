package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class DragonCompanion extends AbstractCosmetic {

    EntityDragon entityDragon;
    private float scale;

    public DragonCompanion() {
        super(false, EnumPurchaseType.DRAGON_COMPANION);
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


        float partialTicks = event.getPartialTicks();

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        GlStateManager.pushMatrix();


        GlStateManager.translate(d0 * scale, d1 * scale, d2 * scale);
        GlStateManager.scale(scale, scale, scale);

//        GlStateManager.rotate(90,1,0,0)
        renderManager.renderEntitySimple(entity, event.getPartialTicks());
        GlStateManager.popMatrix();
        //render
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        tick();
    }

    public void tick() {


        if (entityDragon != null) {
            double animationLen = 3000F;
            double animationPercent = (System.currentTimeMillis() % animationLen) / animationLen;

            float animPi = (float) (animationPercent * Math.PI * 2);


            entityDragon.posX = MathHelper.cos(animPi) * 2D / scale;
            entityDragon.posY = 5;
            entityDragon.posZ = MathHelper.sin(animPi) * 2D / scale;
            entityDragon.lastTickPosX = MathHelper.cos(animPi) * 2D / scale;
            entityDragon.lastTickPosY = 5;
            entityDragon.lastTickPosZ = MathHelper.sin(animPi) * 2D / scale;

            entityDragon.onLivingUpdate();
        }


    }

    Entity getEntity(float partialTicks, EntityPlayer player, RenderManager renderManager, double x, double y, double z) {
        if (entityDragon == null) {
            entityDragon = new EntityDragon(player.getEntityWorld());
        }
        entityDragon.setWorld(player.getEntityWorld());
        entityDragon.ticksExisted = 1;
        return entityDragon;
    }
}
