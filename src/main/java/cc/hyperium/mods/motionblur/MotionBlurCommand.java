package cc.hyperium.mods.motionblur;

import java.lang.reflect.Method;

import cc.hyperium.commands.CommandException;
import cc.hyperium.config.Settings;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import cc.hyperium.commands.BaseCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.math.NumberUtils;

public class MotionBlurCommand implements BaseCommand {
    private Minecraft mc;

    public MotionBlurCommand() {
        this.mc = Minecraft.getMinecraft();
    }

    public void onExecute(String[] args) {
        if (args.length == 0) {
            mc.thePlayer.addChatMessage(new ChatComponentText("Usage: /motionblur <0 - 7>."));
        } else {
            int amount = NumberUtils.toInt(args[0], -1);
            if (amount >= 0 && amount <= 7) {
                if (MotionBlurMod.isFastRenderEnabled()) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("Motion blur does not work if Fast Render is enabled, please disable it in Options > Video Settings > Performance."));
                } else {

                    if (amount != 0) {
                        Settings.MOTION_BLUR_ENABLED = true;
                        Settings.MOTION_BLUR_AMOUNT = (double) amount;

                        try {
                            MotionBlurMod.applyShader();
                            mc.thePlayer.addChatMessage(new ChatComponentText("Motion blur enabled."));
                        } catch (Throwable var5) {
                            mc.thePlayer.addChatMessage(new ChatComponentText("Failed to enable Motion blur."));
                            var5.printStackTrace();
                        }
                    } else {
                        Settings.MOTION_BLUR_ENABLED = false;
                    }
                }
            } else {
                mc.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Invalid amount."));
            }
        }

    }

    public String getName() {
        return "motionblur";
    }
    public String getUsage() {
        return "/motionblur <0 - 7>";
    }
}
