package cc.hyperium.addons.autodab;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.autodab.command.AutoDabCommand;
import cc.hyperium.addons.autodab.utilities.AutoDabUtil;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerChatEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.animation.AbstractAnimationHandler;
import cc.hyperium.handlers.handlers.animation.DabHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Listener for the AutoDab addon
 */
public class EventListener {

    @InvokeEvent
    public void onInit(final InitializationEvent e) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new AutoDabCommand());
    }

    @InvokeEvent
    public void onChat(final ServerChatEvent e) {
        if (!Settings.AUTO_DAB_ENABLED || !AutoDabUtil.hasWonGame(e.getChat().getUnformattedText())) {
            return;
        }
        AutoDab.INSTANCE.setCurrentlyDabbing(true);
        if (Settings.AUTO_DAB_THIRD_PERSON) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        }
        final UUID playerUUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        final DabHandler dabHandler = Hyperium.INSTANCE.getHandlers().getDabHandler();
        final AbstractAnimationHandler.AnimationState state = dabHandler.get(playerUUID);
        state.setToggled(true);
        dabHandler.startAnimation(playerUUID);
        Multithreading.schedule(() -> {
            if (Settings.AUTO_DAB_THIRD_PERSON) {
                Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
            }
            AutoDab.INSTANCE.setCurrentlyDabbing(false);
            dabHandler.stopAnimation(playerUUID);
        }, (long) Settings.AUTO_DAB_LENGTH, TimeUnit.SECONDS);
    }

    @InvokeEvent
    public void onWorldSwitch(final WorldChangeEvent e) {
        if (!Settings.AUTO_DAB_ENABLED) {
            return;
        }
        AutoDab.INSTANCE.setCurrentlyDabbing(false);
        if (Settings.AUTO_DAB_THIRD_PERSON) {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        }
        Hyperium.INSTANCE.getHandlers().getDabHandler().stopAnimation(Minecraft.getMinecraft().getSession().getProfile().getId());
    }
}
