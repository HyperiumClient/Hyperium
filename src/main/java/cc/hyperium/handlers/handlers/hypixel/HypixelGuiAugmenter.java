package cc.hyperium.handlers.handlers.hypixel;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ActionPerformedEvent;
import cc.hyperium.event.InitGuiEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import cc.hyperium.mixins.gui.IMixinGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class HypixelGuiAugmenter {

    private final HashMap<GuiButton, Consumer<GuiButton>> lobbyAdds = new HashMap<>();

    public HypixelGuiAugmenter() {
        lobbyAdds.put(new GuiButton(500001, 1, 1, 100, 20, "View Quests"), button -> {
            try {
                new PlayerQuestsGui(Hyperium.INSTANCE.getHandlers().getDataHandler().getCurrentUser().get()).show();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        lobbyAdds.put(new GuiButton(500002, 1, 22, 100, 20, "View Friends"), button -> new HypixelFriendsGui().show());

    }

    @InvokeEvent
    public void guiOpen(InitGuiEvent event) {
        GuiScreen gui = event.getScreen();
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            String location = Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation();
            if (location.toLowerCase().contains("lobby")) {
                //in a lobby
                if (gui instanceof GuiContainer) {
                    modifyLobbyGui(gui);
                }
            }
        }
//        modifyLobbyGui(gui);
    }

    private boolean shouldDoTheThing() {
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            String location = Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation();
            if (location.toLowerCase().contains("lobby")) {
                //in a lobby
                return gui instanceof GuiContainer;
            }
        }
        return false;
    }

    public void modifyLobbyGui(GuiScreen screen) {
        if (screen == null)
            return;
        for (GuiButton guiButton : lobbyAdds.keySet()) {
            ((IMixinGuiScreen) screen).getButtonList().add(guiButton);
            guiButton.visible = true;
        }
    }


    @InvokeEvent
    public void actionPerformed(ActionPerformedEvent event) {
        Consumer<GuiButton> guiButtonConsumer = lobbyAdds.get(event.getButton());
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(event.getButton());
        }
    }
}
