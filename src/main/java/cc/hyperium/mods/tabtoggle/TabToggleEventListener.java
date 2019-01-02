package cc.hyperium.mods.tabtoggle;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.lwjgl.input.Keyboard;

public class TabToggleEventListener {

    @InvokeEvent
    public void onKeyPress(KeypressEvent e) {
        if (!Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isPressed()) {
            return;
        }

        TabToggleSettings.TAB_TOGGLED = !TabToggleSettings.TAB_TOGGLED;
    }

    @InvokeEvent
    public void onRenderHUD(RenderHUDEvent e) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(0);

        if (TabToggleSettings.ENABLED && TabToggleSettings.TAB_TOGGLED && !Minecraft
            .getMinecraft().gameSettings.keyBindPlayerList.isKeyDown()) {
            if (Minecraft.getMinecraft().isIntegratedServerRunning()
                && Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().size() <= 1
                && scoreObjective == null) {
                Minecraft.getMinecraft().ingameGUI.getTabList().updatePlayerList(false);
            } else {
                Minecraft.getMinecraft().ingameGUI.getTabList().updatePlayerList(true);
                Minecraft.getMinecraft().ingameGUI.getTabList()
                    .renderPlayerlist(sr.getScaledWidth(), scoreboard, scoreObjective);
            }
        }
    }

}
