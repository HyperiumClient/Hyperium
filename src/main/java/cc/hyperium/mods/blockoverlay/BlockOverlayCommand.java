package cc.hyperium.mods.blockoverlay;

import cc.hyperium.commands.BaseCommand;

public class BlockOverlayCommand implements BaseCommand {
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
        BlockOverlay.openGui = true;
    }
}
