package cc.hyperium.mods.motionblur;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;
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

    public static boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(Minecraft.getMinecraft());
        } catch (Exception var1) {
            return false;
        }
    }

    public static void applyShader() {
        HyperiumEntityRenderer.INSTANCE.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }

    @Override
    public AbstractMod init() {
        this.mc = Minecraft.getMinecraft();
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
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : var2) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map) field
                                .get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }

        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }

    }
}
