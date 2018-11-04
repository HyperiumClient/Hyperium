package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;

public class BlockOverlayCommand implements BaseCommand {
    private BlockOverlay mod;

    public BlockOverlayCommand(BlockOverlay mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "blockoverlay";
    }

    @Override
    public String getUsage() {
        return "/blockoverlay";
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new BlockOverlayGui(this.mod));
    }

}
