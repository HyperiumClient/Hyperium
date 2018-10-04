package cc.hyperium.addons.pingdisplay;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.pingdisplay.commands.PingDisplayCommand;
import cc.hyperium.addons.pingdisplay.render.PingDisplayRender;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;

public class PingDisplayAddon extends AbstractAddon {

    @ConfigOpt
    public static boolean showPingDisplay;
    @ConfigOpt
    public static int pingDisplayPosX;
    @ConfigOpt
    public static int pingDisplayPosY;
    public static PingDisplayRender render;

    @Override
    public AbstractAddon init() {
        Hyperium.CONFIG.register(new PingDisplayAddon());
        PingDisplayAddon.render = new PingDisplayRender();
        EventBus.INSTANCE.register(new PingDisplayRender());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new PingDisplayCommand());
        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "PingDisplay", "1.0", "chachy");
        metadata.setDescription("Onscreen ping display.");
        return metadata;
    }

    static {
        PingDisplayAddon.pingDisplayPosX = 20;
        PingDisplayAddon.pingDisplayPosY = 20;
    }
}
