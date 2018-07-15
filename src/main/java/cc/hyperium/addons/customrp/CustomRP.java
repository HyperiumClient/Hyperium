package cc.hyperium.addons.customrp;

import cc.hyperium.Hyperium;
import cc.hyperium.event.*;
import cc.hyperium.internal.addons.IAddon;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import cc.hyperium.addons.customrp.config.Config;
import cc.hyperium.addons.customrp.utils.AddonUpdateChecker;
import cc.hyperium.addons.customrp.utils.RichPresenceUpdater;

import static com.jagrosh.discordipc.entities.pipe.PipeStatus.CONNECTED;

public class CustomRP implements IAddon {
    private IPCClient client;

    @Override
    public void onLoad() {
        System.out.println("[CustomRP] Addon loaded");
        EventBus.INSTANCE.register(this);
        EventBus.INSTANCE.register(new AddonUpdateChecker());
        Hyperium.CONFIG.register(new Config());
    }

    @InvokeEvent
    public void init(InitializationEvent event) {
        RichPresenceUpdater.callCustomRPUpdate();
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void initHighPriority(InitializationEvent event) {
        this.client = new IPCClient(412963310867054602L);
        this.client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                EventBus.INSTANCE.register(new RichPresenceUpdater(client));
            }
        });
        try {
            client.connect(DiscordBuild.ANY); } catch (NoDiscordClientException e) {
            Hyperium.LOGGER.warn("[CustomRP] No Discord clients found");
        }
    }

    @Override
    public void onClose() {
        System.out.println("[CustomRP] Addon closed");

        try {
            if (this.client != null && this.client.getStatus() == CONNECTED) {
                this.client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendDebugInfo() {
    }
}
