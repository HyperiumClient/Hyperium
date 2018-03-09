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

    private List<Entity> rendered = new ArrayList<>();

    @ConfigOpt
    private boolean limitArmourStands = true;

    @InvokeEvent
    public void renderHUD(RenderHUDEvent event) {
        rendered.clear();
    }

    public boolean shouldRender(Entity entity) {
        if (entity instanceof EntityArmorStand && limitArmourStands) {
            return isSimilar(entity);

        }
        return true;
    }

    private boolean isSimilar(Entity other) {

        String text1 = EnumChatFormatting.getTextWithoutFormattingCodes(other.getDisplayName().getUnformattedText());
        double posX = other.posX;
        double posY = other.posY;
        double posZ = other.posZ;
        for (Entity entity : rendered) {
            String text = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getDisplayName().getUnformattedText());
            if (text.equalsIgnoreCase(text1) && posX == entity.posX && posY == entity.posY && entity.posZ == posZ) {
                return true;
            }

        }
        return false;
    }
}
