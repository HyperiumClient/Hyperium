package cc.hyperium.addons.customrp;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.customrp.utils.Mode;
import cc.hyperium.event.*;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import cc.hyperium.addons.customrp.config.Config;
import cc.hyperium.addons.customrp.utils.RichPresenceUpdater;

import static com.jagrosh.discordipc.entities.pipe.PipeStatus.CONNECTED;

public class CustomRP extends AbstractAddon {
    private IPCClient client;

    @Override
    public AbstractAddon init() {
        //High Priority
        this.client = new IPCClient(412963310867054602L);
        this.client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                EventBus.INSTANCE.register(new RichPresenceUpdater(client));
            }
        });
        try {
            client.connect(DiscordBuild.ANY); } catch (RuntimeException | NoDiscordClientException e) {
            Hyperium.LOGGER.warn("[CustomRP] No Discord clients found");
        }
        
        //Medium priority
        RichPresenceUpdater.callCustomRPUpdate();
        EventBus.INSTANCE.register(this);
        Hyperium.CONFIG.register(new Config());

        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "CustomRP", "1.4.0", "SHARDcoder");
        metadata.setDisplayName(ChatColor.DARK_GRAY + "CustomRP");
        metadata.setOverlayClassPath("cc.hyperium.addons.customrp.gui.Overlay");
        metadata.setDescription("Customise the Rich Presence of Hyperium");

        return metadata;
    }

    @InvokeEvent
    public void init(InitializationEvent event) {
        Multithreading.POOL.submit(() -> {
            try {
                Thread.sleep(2500L);

                Mode.set(Config.CUSTOM_RP_MODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        try {
            if (this.client != null && this.client.getStatus() == CONNECTED) {
                this.client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
