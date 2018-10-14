package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.ThreadLocalRandom;

public class DragonCompanion extends AbstractCosmetic {

    EntityDragon entityDragon;
    private float scale;
    private AnimationState animationState = new AnimationState();

    public DragonCompanion() {
        super(false, EnumPurchaseType.DRAGON_COMPANION);
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        if (Minecraft.getMinecraft().theWorld == null)
            return;
        if (!isPurchasedBy(event.getEntity().getUniqueID()))
            return;
        if (PurchaseApi.getInstance().getSelf().getCachedSettings().getCurrentCompanion() != EnumPurchaseType.DRAGON_COMPANION) {
            return;
        }
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
        if(Minecraft.getMinecraft().theWorld == null)
            return;

        if (entityDragon != null) {
            entityDragon.lastTickPosX = entityDragon.posX;
            entityDragon.lastTickPosY = entityDragon.posY;
            entityDragon.lastTickPosZ = entityDragon.posZ;

            AnimationPoint current = animationState.getCurrent(false);
            entityDragon.posX = current.x / scale;
            entityDragon.posY = current.y / scale;
            entityDragon.posZ = current.z / scale;

            double dx = animationState.next.x - animationState.last.x;
            double dz = animationState.next.z - animationState.last.z;

            double angrad = Math.atan2(dx, -dz);
            double angle = Math.toDegrees(angrad);
            entityDragon.prevRotationYaw = entityDragon.rotationYaw;
            entityDragon.rotationYaw = (float) angle;

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

    class AnimationPoint {
        double x, y, z;

        public AnimationPoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distanceSqTo(AnimationPoint other) {
            return Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2) + Math.pow(other.z - z, 2);
        }

        public double distanceTo(AnimationPoint animationPoint) {
            return Math.sqrt(distanceSqTo(animationPoint));
        }
    }

    class AnimationState {
        private final int BOUNDS = 5;
        //Speed in blocks per second
        private final double speed = 3D;
        AnimationPoint last;
        AnimationPoint next;
        private long start = 0L;
        private double currentDistance = 0;
        private long totalTime = 0;
        private long endTime;

        public AnimationState() {
            next = generateRandom();
            switchToNext();
        }

        public void switchToNext() {
            last = next;
            next = generateRandom();
            start = System.currentTimeMillis();
            currentDistance = next.distanceTo(last);
            totalTime = (long) (currentDistance / speed * 1000);
            endTime = start + totalTime;


        }

        public AnimationPoint getCurrent(boolean last) {
            long l = System.currentTimeMillis();
            if (last)//back 1 tick
                l -= 50L;
            if (l > endTime) {
                switchToNext();
            }
            double percent = (double) (l - start) / (double) totalTime;
            return new AnimationPoint(interpolate(this.last.x, next.x, percent),
                    interpolate(this.last.y, next.y, percent),
                    interpolate(this.last.z, next.z, percent));
        }

        private double interpolate(final double now, final double then, final double percent) {
            return (now + (then - now) * percent);
        }

        private AnimationPoint generateRandom() {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            return new AnimationPoint(current.nextDouble(-BOUNDS, BOUNDS), current.nextDouble(0, BOUNDS), current.nextDouble(-BOUNDS, BOUNDS));
        }
    }

}
