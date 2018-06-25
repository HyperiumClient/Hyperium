package cc.hyperium.handlers.handlers.particle;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PurchaseLoadEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.particle.animations.DoubleHelixAnimation;
import cc.hyperium.handlers.handlers.particle.animations.ExplodeAnimation;
import cc.hyperium.handlers.handlers.particle.animations.QuadHelixAnimation;
import cc.hyperium.handlers.handlers.particle.animations.TripleHelixAnimation;
import cc.hyperium.mixinsimp.renderer.IMixinEntityFx;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.util.*;

/**
 * Created by mitchellkatz on 6/23/18. Designed for production use on Sk1er.club
 */
public class ParticleAuraHandler {

    private final ArrayList<EnumParticleTypes> particleTypes = new ArrayList<>();
    private HashMap<UUID, ParticleAura> auras = new HashMap<>();
    private HashMap<String, AbstractAnimation> animations = new HashMap<>();

    public ParticleAuraHandler() {
        particleTypes.addAll(Arrays.asList(EnumParticleTypes.values()));
        particleTypes.remove(EnumParticleTypes.BARRIER);
        animations.put("triple_helix", new TripleHelixAnimation());
        animations.put("double_helix", new DoubleHelixAnimation());
        animations.put("quad_helix", new DoubleHelixAnimation());
        animations.put("explode", new ExplodeAnimation());
    }

    public HashMap<UUID, ParticleAura> getAuras() {
        return auras;
    }

    public HashMap<String, AbstractAnimation> getAnimations() {
        return animations;
    }

    public ArrayList<EnumParticleTypes> getParticleTypes() {
        return particleTypes;
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

        auras.put(purchaseLoadEvent.getUuid(), new ParticleAura(id, particle_animation, data.optInt("max_age", 2)));

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
    public void renderPlayer(RenderPlayerEvent event) {
        AbstractClientPlayer entity = event.getEntity();
        ParticleAura particleAura = auras.get(entity.getUniqueID());
        if (entity.equals(Minecraft.getMinecraft().thePlayer)) {
            particleAura = new ParticleAura(EnumParticleTypes.CRIT, new QuadHelixAnimation(), 200);
        }

        if (particleAura != null) {
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            List<Vec3> render = particleAura.render(entity, x, y, z);
            for (Vec3 vec3 : render) {
                Map<Integer, IParticleFactory> particleMap = ((IMixinEffectRenderer) Minecraft.getMinecraft().effectRenderer).getParticleMap();
                int particleID = particleAura.getType().getParticleID();
                IParticleFactory iParticleFactory = particleMap.get(particleID);
                if (iParticleFactory != null) {
                    EntityFX entityFX = iParticleFactory.getEntityFX(particleID, entity.getEntityWorld(), vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D, 1.0D, 1.0D);
                    entityFX.onUpdate();
                    int particleMaxAge = particleAura.getParticleMaxAge();
                    ((IMixinEntityFx) entityFX).setMaxAge(particleMaxAge);
                    Minecraft.getMinecraft().effectRenderer.addEffect(entityFX);
                }
            }
        }
    }


}
