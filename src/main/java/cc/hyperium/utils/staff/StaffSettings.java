package cc.hyperium.utils.staff;

import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;

public class StaffSettings {

    private StaffUtils.DotColour dotColour;
    private String easterEggEntityPath;

    public StaffSettings(StaffUtils.DotColour dotColour, String easterEggEntityPath) {
        if (dotColour != null)
            this.dotColour = dotColour;
        else
            this.dotColour = new StaffUtils.DotColour(false, ChatColor.GREEN);
        if (easterEggEntityPath != null)
            this.easterEggEntityPath = easterEggEntityPath;
        else
            this.easterEggEntityPath = "None";
    }

    public StaffUtils.DotColour getDotColour() {
        return dotColour;
    }

    public String getEasterEggEntityPath() {
        return easterEggEntityPath;
    }

    public boolean hasEasterEggEntityPath() {
        return !easterEggEntityPath.equalsIgnoreCase("None");
    }
}
