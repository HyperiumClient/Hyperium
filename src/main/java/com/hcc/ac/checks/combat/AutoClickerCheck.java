/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.ac.checks.combat;

import com.hcc.ac.User;
import com.hcc.ac.checks.CheckResult;
import com.hcc.ac.checks.ICheck;

public class AutoClickerCheck implements ICheck {
    @Override
    public CheckResult check(User user) {
        if(user.getAps() > 20)
            return new CheckResult(CheckResult.Level.SLIGHTLY, "AutoClicker",user.getAps()+" APS");
        else if(user.getAps() > 25)
            return new CheckResult(CheckResult.Level.POTENTIALLY,"AutoClicker", user.getAps()+" APS");
        else if(user.getAps() > 35)
            return new CheckResult(CheckResult.Level.DEFINITELY,"AutoClicker", user.getAps()+" APS");
        else
            return new CheckResult(CheckResult.Level.CLEAN, "AutoClicker", "Passed");
    }
}
