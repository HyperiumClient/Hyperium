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

package cc.hyperium.mods.motionblur;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mixinsimp.client.renderer.HyperiumEntityRenderer;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.motionblur.resource.MotionBlurResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.Map;


public class MotionBlurMod extends AbstractMod {

    private Minecraft mc;
    private Map domainResourceManagers;

    static boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(Minecraft.getMinecraft().gameSettings);
        } catch (Exception var1) {
            return false;
        }
    }

    static void applyShader() {
        HyperiumEntityRenderer.INSTANCE.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }

    @Override
    public AbstractMod init() {
        mc = Minecraft.getMinecraft();
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler()
                .registerCommand(new MotionBlurCommand());
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Motion Blur Mod", "1.0", "Chachy, Amp, Koding");
    }

    @InvokeEvent
    public void onClientTick(TickEvent event) {
        if (Settings.MOTION_BLUR_ENABLED && !Minecraft.getMinecraft().entityRenderer.isShaderActive() && mc.theWorld != null && !isFastRenderEnabled()) {
            applyShader();
        }

        if (domainResourceManagers == null) {
            try {
                Field[] var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : var2) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        domainResourceManagers = (Map) field.get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }

        if (!domainResourceManagers.containsKey("motionblur")) {
            domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
    }
}
