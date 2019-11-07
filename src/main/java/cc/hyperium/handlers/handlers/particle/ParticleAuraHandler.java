/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.particle;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.PurchaseLoadEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.gui.GuiHyperiumScreenIngameMenu;
import cc.hyperium.handlers.handlers.particle.animations.*;
import cc.hyperium.mixins.client.particle.IMixinEntityFX;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.List;
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
        Arrays.stream(EnumParticleType.values()).forEach(enumParticleType -> renderEngines.put(enumParticleType, enumParticleType.getParticle()));

        animations.put("Double Helix", new DoubleHelix());
        animations.put("Double Twirl", new DoubleTwirlAnimation());
        animations.put("Triple Twirl", new TripleTwirlAnimation());
        animations.put("Quad Twirl", new QuadTwirlAnimation());
        animations.put("Static Trail", new StaticTrailAnimation());
        animations.put("Explode", new ExplodeAnimation());
        animations.put("Vortex of doom", new VortexOfDoomAnimation());
        animations.put("Tornado", new TornadoAnimation());

    }

    @InvokeEvent
    public void loadPurchaseEvent(PurchaseLoadEvent purchaseLoadEvent) {
        HyperiumPurchase purchase = purchaseLoadEvent.getPurchase();
        auras.remove(purchase.getPlayerUUID());
        JsonHolder purchaseSettings = purchase.getPurchaseSettings();
        if (!purchaseSettings.has("particle")) return;

        JsonHolder data = purchaseSettings.optJSONObject("particle");
        String particle_animation1 = data.optString("particle_animation");
        AbstractAnimation particle_animation = animations.get(particle_animation1);

        EnumParticleType type = EnumParticleType.parse(data.optString("type"));
        if (particle_animation == null || type == null) return;

        if (!purchase.hasPurchased("PARTICLE_" + type.name()) || !purchase.hasPurchased(
            "ANIMATION_" + (particle_animation1.toUpperCase().replace(" ", "_")))) {
            return;
        }

        boolean rgb = data.optBoolean("rgb");
        boolean chroma = data.optBoolean("chroma");
        ParticleAura max_age = new ParticleAura(renderEngines.get(type), particle_animation, data.optInt("max_age", 2), chroma, rgb);
        max_age.setRgb(data.optInt("red"), data.optInt("green"), data.optInt("blue"));
        auras.put(purchase.getPlayerUUID(), max_age);
    }

    @InvokeEvent
    public void worldSwapEvent(WorldChangeEvent event) {
        ParticleAura particleAura = auras.get(UUIDUtil.getClientUUID());
        auras.clear();
        if (particleAura != null) auras.put(UUIDUtil.getClientUUID(), particleAura);
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        if (Minecraft.getMinecraft().isGamePaused()) return;
        if (event.getEntity().isInvisible()) return;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHyperiumScreenIngameMenu) return;
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        if (!Settings.SHOW_PARTICLES) return;

        AbstractClientPlayer entity = event.getEntity();
        if (!entity.equals(Minecraft.getMinecraft().thePlayer) && (entity.posX != entity.prevPosZ || entity.posY != entity.prevPosY || entity.posZ != entity.prevPosZ)) {
            return;
        }

        ParticleAura particleAura = auras.get(entity.getUniqueID());
        if (particleAura != null && !entity.isInvisible()) {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * event.getPartialTicks();
            double y = entity.posY + (entity.posY - entity.prevPosY) * event.getPartialTicks();
            double z = entity.posZ + (entity.posZ - entity.prevPosZ) * event.getPartialTicks();
            List<Vec3> render = particleAura.render(entity, x, y, z);

            render.forEach(vec3 -> {
                IParticle type = particleAura.getType();

                if (type != null) {
                    EntityFX entityFX = type.spawn(entity.worldObj, vec3.xCoord, vec3.yCoord, vec3.zCoord);
                    int particleMaxAge = particleAura.getParticleMaxAge();
                    IMixinEntityFX e = (IMixinEntityFX) entityFX;

                    if (particleAura.isChroma()) {
                        int i = Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
                        Color color = new Color(i);
                        entityFX.setRBGColorF(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
                    } else if (particleAura.isRgb()) {
                        entityFX.setRBGColorF(particleAura.getRed() / 255F, particleAura.getBlue() / 255F, particleAura.getBlue() / 255F);
                    }

                    e.setParticleGravity(10);
                    e.setParticleMaxAge(particleMaxAge);
                    Minecraft.getMinecraft().effectRenderer.addEffect(entityFX);
                }
            });
        }
    }

    public EnumMap<EnumParticleType, IParticle> getRenderEngines() {
        return renderEngines;
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
}
