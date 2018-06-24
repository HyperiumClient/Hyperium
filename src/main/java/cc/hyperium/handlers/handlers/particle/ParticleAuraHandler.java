package cc.hyperium.handlers.handlers.particle;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PurchaseLoadEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.particle.animations.DoubleHelixAnimation;
import cc.hyperium.handlers.handlers.particle.animations.TripleHelixAnimation;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by mitchellkatz on 6/23/18. Designed for production use on Sk1er.club
 */
public class ParticleAuraHandler {

    private HashMap<UUID, ParticleAura> auras = new HashMap<>();
    private HashMap<String, AbstractAnimation> animations = new HashMap<>();


    public ParticleAuraHandler() {
        animations.put("triple_helix", new TripleHelixAnimation());
        animations.put("double_helix", new DoubleHelixAnimation());
    }

    @InvokeEvent
    public void loadPurchaseEvent(PurchaseLoadEvent purchaseLoadEvent) {
        JsonHolder purchaseSettings = purchaseLoadEvent.getPurchase().getPurchaseSettings();
        if (!purchaseSettings.has("particle"))
            return;
        JsonHolder data = purchaseSettings.optJSONObject("particle");
        EnumParticleTypes id = EnumParticleTypes.getParticleFromId(data.optInt("particle_type"));
        AbstractAnimation particle_animation = animations.get(data.optString("particle_animation"));
        if (particle_animation == null)
            return;

        auras.put(purchaseLoadEvent.getUuid(), new ParticleAura(id, particle_animation));

    }

    @InvokeEvent
    public void worldSwapEvent(WorldChangeEvent event) {
        ParticleAura particleAura = auras.get(UUIDUtil.getClientUUID());
        auras.clear();
        if (particleAura != null) {
            auras.put(UUIDUtil.getClientUUID(), particleAura);
        }
    }

    @InvokeEvent
    public void renderPlayer(TickEvent event) {
        for (EntityPlayer entity : Minecraft.getMinecraft().theWorld.playerEntities) {


            ParticleAura particleAura = auras.get(entity.getUniqueID());
            if (entity.equals(Minecraft.getMinecraft().thePlayer)) {
                particleAura = new ParticleAura(EnumParticleTypes.HEART, new TripleHelixAnimation());
            }
            if (particleAura != null) {
                double x = entity.posX;
                double y = entity.posY;
                double z = entity.posZ;
                List<Vec3> render = particleAura.render(entity, x, y, z);
                for (Vec3 vec3 : render) {
                    entity.getEntityWorld().spawnParticle(particleAura.getType(),
                            vec3.xCoord,
                            vec3.yCoord,
                            vec3.zCoord, 0, 0, 0);
                }
            }
        }


    }

    protected double interpolate(final double currentPos, final double prevPos, final float percent) {
        return prevPos + (currentPos - prevPos) * percent;
    }

}
