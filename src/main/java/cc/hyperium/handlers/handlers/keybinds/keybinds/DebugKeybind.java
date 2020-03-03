package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumKeybind;
import org.lwjgl.input.Keyboard;

public class DebugKeybind extends HyperiumKeybind {

  public DebugKeybind() {
    super("DEBUG", Keyboard.KEY_M, KeyType.TEST);
  }

  @Override
  public void onPress() {
    GeneralChatHandler.instance().sendMessage("hahaah ahahaha why you using this");
  }

}
