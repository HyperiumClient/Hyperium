package cc.hyperium.handlers.handlers.particle;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PurchaseLoadEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.particle.animations.*;
import cc.hyperium.handlers.handlers.particle.particle.ChromaRedstoneParticle;
import cc.hyperium.mixinsimp.renderer.IMixinEntityFx;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EntityFX;
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
    private EnumMap<EnumParticleType, IParticle> renderEngines = new EnumMap<>(EnumParticleType.class);

    public ParticleAuraHandler() {
        renderEngines.put(EnumParticleType.CHROMA_DUST, new ChromaRedstoneParticle());
        particleTypes.addAll(Arrays.asList(EnumParticleTypes.values()));
        particleTypes.remove(EnumParticleTypes.BARRIER);
        animations.put("triple_helix", new TripleTwirlAnimation());
        animations.put("double_helix", new DoubleTwirlAnimation());
        animations.put("quad_helix", new DoubleTwirlAnimation());
        animations.put("static_tail", new StaticTrailAnimation());
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
        AbstractAnimation particle_animation = animations.get(data.optString("particle_animation"));
        EnumParticleType type = EnumParticleType.valueOf(data.optString("type"));
        if (particle_animation == null)
            return;

        auras.put(purchaseLoadEvent.getUuid(), new ParticleAura(renderEngines.get(type), particle_animation, data.optInt("max_age", 2)));

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
            particleAura = new ParticleAura(new ChromaRedstoneParticle(), new ExplodeAnimation(), 2);
        }

        if (particleAura != null) {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * event.getPartialTicks();
            double y = entity.posY + (entity.posY - entity.prevPosY) * event.getPartialTicks();
            double z = entity.posZ + (entity.posZ - entity.prevPosZ) * event.getPartialTicks();
            List<Vec3> render = particleAura.render(entity, x, y, z);
            for (Vec3 vec3 : render) {
                IParticle type = particleAura.getType();
                if (type != null) {
                    EntityFX entityFX = type.spawn(entity.worldObj, vec3.xCoord, vec3.yCoord, vec3.zCoord);
                    int particleMaxAge = particleAura.getParticleMaxAge();
                    ((IMixinEntityFx) entityFX).setMaxAge(particleMaxAge);
                    Minecraft.getMinecraft().effectRenderer.addEffect(entityFX);
                }
            }
        }
    }


}
