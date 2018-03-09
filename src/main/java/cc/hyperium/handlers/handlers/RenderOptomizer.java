package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class RenderOptomizer {

    private List<String> rendered = new ArrayList<>();

    @ConfigOpt
    private boolean limitArmourStands = true;

    @InvokeEvent
    public void renderHUD(RenderHUDEvent event) {
        rendered.clear();
    }

    public boolean shouldRender(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getDisplayName().getUnformattedText());
            boolean flag = true;
            if (rendered.contains(textWithoutFormattingCodes)) {
                flag = false;
            } else rendered.add(textWithoutFormattingCodes);
            return flag;


        }
        return true;
    }
}
