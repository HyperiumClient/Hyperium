package cc.hyperium.mods.blockoverlay;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;

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
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        this.mod.mc.displayGuiScreen(new BlockOverlayGui(this.mod));
        EventBus.INSTANCE.unregister(this);
    }
}
