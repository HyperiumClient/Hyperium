package cc.hyperium.cosmetics.companions.dragon;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEntitiesEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DragonCompanion extends AbstractCosmetic {

    private float scale;
    private HashMap<EntityPlayer, CustomDragon> dragonHashMap = new HashMap<>();

    public DragonCompanion() {
        super(false, EnumPurchaseType.DRAGON_COMPANION);
    }

    @InvokeEvent
    public void renderEntities(RenderEntitiesEvent entitiesEvent) {
        if (Settings.SHOW_COMPANION_IN_1ST_PERSON) {
            renderPlayer(new RenderPlayerEvent(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().getRenderManager(), 0, 0, 0,
                entitiesEvent.getPartialTicks()));
        }
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        if (Minecraft.getMinecraft().theWorld == null)
            return;
        if (!isPurchasedBy(event.getEntity().getUniqueID()))
            return;
        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
        if (packageIfReady == null)
            return;
        if (packageIfReady.getCachedSettings().getCurrentCompanion() != EnumPurchaseType.DRAGON_COMPANION) {
            return;
        }
        scale = .1F;
        AbstractClientPlayer player = event.getEntity();
        CustomDragon customDragon = dragonHashMap.computeIfAbsent(event.getEntity(), player1 -> {
            EntityDragon dragon = new EntityDragon(player1.getEntityWorld());
            dragon.setSilent(true);
            return new CustomDragon(dragon, new AnimationState());
        });
        Entity entity = customDragon.dragon;
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        //Manage pos here;

        float partialTicks = event.getPartialTicks();

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        GlStateManager.pushMatrix();

//        GlStateManager.translate(event.getX(), event.getY(), event.getZ());
        EntityDragon entityDragon = customDragon.dragon;
        AnimationState animationState = customDragon.animationState;
        AnimationPoint current = animationState.getCurrent(player);
        entityDragon.posX = current.x / scale;
        entityDragon.posY = current.y / scale;
        entityDragon.posZ = current.z / scale;
        GlStateManager.translate(-((IMixinRenderManager) renderManager).getPosX(),
            -((IMixinRenderManager) renderManager).getPosY(),
            -((IMixinRenderManager) renderManager).getPosZ());

        GlStateManager.translate(d0 * scale, d1 * scale, d2 * scale);
        GlStateManager.scale(scale, scale, scale);

        renderManager.renderEntitySimple(entity, event.getPartialTicks());
        GlStateManager.popMatrix();
        //render
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        tick();
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        dragonHashMap.clear();
    }

    public void tick() {
        if (Minecraft.getMinecraft().theWorld == null)
            return;

        for (EntityPlayer player : dragonHashMap.keySet()) {
            CustomDragon customDragon = dragonHashMap.get(player);
            EntityDragon entityDragon = customDragon.dragon;
            AnimationState animationState = customDragon.animationState;
            if (entityDragon != null) {
                entityDragon.setWorld(player.getEntityWorld());
                double v = animationState.next.distanceSqTo(new AnimationPoint(player.posX, player.posY, player.posZ));
                if (v > 7 * 7) {
                    animationState.switchToNext(player, true);
                }

                entityDragon.lastTickPosX = entityDragon.posX;
                entityDragon.lastTickPosY = entityDragon.posY;
                entityDragon.lastTickPosZ = entityDragon.posZ;
                entityDragon.prevRotationYawHead = entityDragon.rotationYawHead;

                AnimationPoint current = animationState.getCurrent(player);
                entityDragon.posX = current.x / scale;
                entityDragon.posY = current.y / scale;
                entityDragon.posZ = current.z / scale;

                double dx = animationState.next.x - animationState.last.x;
                double dz = animationState.next.z - animationState.last.z;

                double angrad = Math.atan2(dx, -dz);
                double angle = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(angrad));

                if (animationState.nextFrameisNewPoint(player)) {
                    double dx1 = animationState.nextNext.x - animationState.next.x;
                    double dz1 = animationState.nextNext.z - animationState.next.z;
                    double angrad1 = Math.atan2(dx1, -dz1);
                    double angle1 = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(angrad1));
                    //Average yaw
                    angle = ((float) angle + (float) angle1) / 2;
                    entityDragon.rotationYawHead = (float) angle1;
                }
                entityDragon.prevRotationYaw = entityDragon.rotationYaw;
                entityDragon.rotationYaw = (float) angle;

                entityDragon.onLivingUpdate();
            }
        }

    }

    class CustomDragon {
        EntityDragon dragon;
        AnimationState animationState;

        public CustomDragon(EntityDragon dragon, AnimationState point) {
            this.dragon = dragon;
            this.animationState = point;
        }
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
        private final int BOUNDS = 4;
        AnimationPoint last;
        AnimationPoint next;
        AnimationPoint nextNext;
        //Speed in blocks per second
        private double speed = 3D;
        private long start = 0L;
        private double currentDistance = 0;
        private long totalTime = 0;
        private long endTime;

        public AnimationState() {
            next = generateRandom(null);
            switchToNext(null, false);
        }

        public void switchToNext(EntityPlayer player, boolean toofar) {
            if (nextNext == null)
                nextNext = toofar ? new AnimationPoint(player.posX, player.posY + 3, player.posZ) : generateRandom(player);
            last = toofar ? getCurrent(player) : next;
            next = toofar ? new AnimationPoint(player.posX, player.posY + 3, player.posZ) : nextNext;
            start = System.currentTimeMillis();
            currentDistance = next.distanceTo(last);
            if (toofar) {
                speed = currentDistance;
            } else
                speed = 3;
            totalTime = (long) (currentDistance / speed * 1000);
            endTime = start + totalTime;

        }

        public AnimationPoint getCurrent(EntityPlayer player) {
            long l = System.currentTimeMillis();
            if (l > endTime) {
                switchToNext(player, false);
            }
            double percent = (double) (l - start) / (double) totalTime;
            return new AnimationPoint(interpolate(this.last.x, next.x, percent),
                interpolate(this.last.y, next.y, percent),
                interpolate(this.last.z, next.z, percent));
        }

        public boolean nextFrameisNewPoint(EntityPlayer player) {
            long endTime = this.endTime;
            boolean b = System.currentTimeMillis() + 50L >= endTime;
            if (b) {
                nextNext = generateRandom(player);
            }
            return b;
        }

        private double interpolate(final double now, final double then, final double percent) {
            return (now + (then - now) * percent);
        }

        private AnimationPoint generateRandom(EntityPlayer player) {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            double posX = player == null ? 0 : player.posX;
            double posY = player == null ? 0 : player.posY;
            double posZ = player == null ? 0 : player.posZ;
            double y = current.nextDouble(.5 + posY, posY + BOUNDS + (double) BOUNDS / 2D);
            return new AnimationPoint(current.nextDouble(-BOUNDS + posX, BOUNDS + posX),
                y,
                current.nextDouble(-BOUNDS + posZ, BOUNDS + posZ));
        }

    }

}
