/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.staff;

import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;

public class StaffSettings {

    // Get the staff members color
    private StaffUtils.DotColour dotColour;

    /**
     * Initialize the dot color
     *
     * @param dotColour the staff members dot color
     */
    public StaffSettings(StaffUtils.DotColour dotColour) {
        // If the dot color isn't null, assign it
        if (dotColour != null) this.dotColour = dotColour;

        // Otherwise assume the user doesn't have a custom dot color and set it to green
        else this.dotColour = new StaffUtils.DotColour(false, ChatColor.GREEN);
    }

    /**
     * Used to retrieve the dot color
     *
     * @return the staff members dot color
     */
    public StaffUtils.DotColour getDotColour() {
        return dotColour;
    }
}
