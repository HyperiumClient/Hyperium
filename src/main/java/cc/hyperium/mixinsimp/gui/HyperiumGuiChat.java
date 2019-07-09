package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.HypixelDetector;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

public class HyperiumGuiChat {
    private GuiChat parent;

    public HyperiumGuiChat(GuiChat parent) {
        this.parent = parent;
    }

    public void onSendAutocompleteRequest(String leftOfCursor) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().autoComplete(leftOfCursor);
    }

    public void init(GuiTextField inputField) {
        if (HypixelDetector.getInstance().isHypixel()) {
            inputField.setMaxStringLength(256);
        } else {
            inputField.setMaxStringLength(100);
        }
    }
}
