package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SendChatMessageEvent;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumGuiChat {
  private GuiChat parent;
  public HyperiumGuiChat(GuiChat parent) {
    this.parent = parent;
  }
  public void onSendAutocompleteRequest(String leftOfCursor) {
    Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().autoComplete(leftOfCursor);
  }
  public void init(GuiTextField inputField) {
    Hyperium.INSTANCE.getHandlers().getKeybindHandler().releaseAllKeybinds();
    if (HypixelDetector.getInstance().isHypixel()) {
      inputField.setMaxStringLength(256);
    } else {
      inputField.setMaxStringLength(100);
    }
  }
  public void keyTyped(GuiTextField inputField, CallbackInfo ci) {
    String msg = inputField.getText().trim();
    SendChatMessageEvent event = new SendChatMessageEvent(msg);
    EventBus.INSTANCE.post(event);
    if (!event.isCancelled()) {
      ChatUtil.sendMessage(msg);
    }
    Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
    Minecraft.getMinecraft().displayGuiScreen(null);
    ci.cancel();
  }
}
