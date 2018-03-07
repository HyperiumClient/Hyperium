package cc.hyperium.gui.games;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.GuiButton;

public class HyperiumGuiGames extends HyperiumGui {

    @Override
    protected void pack() {
        reg("DYNO", new GuiButton(0, ResolutionUtil.current().getScaledWidth() / 2 - 100, 40, "Dyno"), guiButton -> {
          new DynoGame().show();
        }, guiButton -> {

        });
    }
}
