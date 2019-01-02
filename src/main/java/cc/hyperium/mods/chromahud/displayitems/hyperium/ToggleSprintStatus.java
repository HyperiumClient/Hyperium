package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.common.ToggleSprintContainer;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.util.StringUtils;
import cc.hyperium.config.Settings;

public class ToggleSprintStatus extends DisplayItem {
    private String sprintEnabledText;

    public ToggleSprintStatus(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.sprintEnabledText = data.optString("sprintEnabledText");
        if (StringUtils.isNullOrEmpty(sprintEnabledText)) {
            if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
                sprintEnabledText = "ToggleSprint Enabled";
            } else {
                sprintEnabledText = "[ToggleSprint] Enabled";
            }
        }
        this.height = 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        if (ToggleSprintContainer.toggleSprintActive) {
            this.width = ElementRenderer.getFontRenderer().getStringWidth(sprintEnabledText);
            ElementRenderer.draw(x, y, sprintEnabledText);
        }
    }

    public String getStatusText() {
        return sprintEnabledText;
    }

    public void setSprintEnabledText(String text) {
        this.sprintEnabledText = text;
        data.put("sprintEnabledText", text);
    }
}
