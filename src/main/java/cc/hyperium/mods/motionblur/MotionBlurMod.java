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
import cc.hyperium.event.interact.KeyPressEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.motionblur.resource.MotionBlurResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Map;


public class MotionBlurMod extends AbstractMod {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<String, FallbackResourceManager> domainResourceManagers = ((SimpleReloadableResourceManager) mc.getResourceManager()).domainResourceManagers;
    private Field cachedFastRender;
    private int ticks;

    @Override
    public AbstractMod init() {
        try {
            cachedFastRender = GameSettings.class.getDeclaredField("ofFastRender");
        } catch (Exception ignored) {
        }

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new MotionBlurCommand());
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Motion Blur Mod", "2.0", "Sk1er LLC");
    }

    @InvokeEvent
    public void onClientTick(TickEvent event) {
        if (domainResourceManagers != null) {
            if (!domainResourceManagers.containsKey("motionblur")) {
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager(mc.metadataSerializer_));
            }
        }

        ++ticks;
        if (ticks % 5000 == 0) {
            if (isFastRenderEnabled() && Settings.MOTION_BLUR_ENABLED) {
                if (mc.thePlayer != null && mc.theWorld != null) {
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Motion Blur is not compatible with OptiFine's Fast Render.");
                }
            }
        }
    }

    @InvokeEvent
    public void onKey(KeyPressEvent event) {
        if (mc.thePlayer != null && Settings.MOTION_BLUR_ENABLED && Keyboard.isKeyDown(mc.gameSettings.keyBindTogglePerspective.getKeyCode())) {
            mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
        }
    }

    public boolean isFastRenderEnabled() {
        try {
            return cachedFastRender.getBoolean(mc.gameSettings);
        } catch (Exception ignored) {
            return false;
        }
    }
}
