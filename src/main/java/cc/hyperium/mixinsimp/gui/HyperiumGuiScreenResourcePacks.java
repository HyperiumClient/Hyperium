package cc.hyperium.mixinsimp.gui;

import net.minecraft.client.gui.*;

import java.util.List;

public class HyperiumGuiScreenResourcePacks  extends GuiScreen{
    private GuiScreenResourcePacks parent;

    public HyperiumGuiScreenResourcePacks(GuiScreenResourcePacks parent){
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList){
        buttonList.forEach(b -> {
            b.setWidth(200);
            if (b.id == 2) {
                b.xPosition = parent.width / 2 - 204;
            }
        });
    }
}
