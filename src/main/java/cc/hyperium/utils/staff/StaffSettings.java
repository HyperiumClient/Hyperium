package cc.hyperium.utils.staff;

import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;

public class StaffSettings {

    private StaffUtils.DotColour dotColour;

    public StaffSettings(StaffUtils.DotColour dotColour) {
        if (dotColour != null)
            this.dotColour = dotColour;
        else
            this.dotColour = new StaffUtils.DotColour(false, ChatColor.GREEN);
    }

    public StaffUtils.DotColour getDotColour() {
        return dotColour;
    }
}
