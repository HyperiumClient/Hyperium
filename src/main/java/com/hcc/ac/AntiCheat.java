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

package com.hcc.ac;

import com.hcc.HCC;
import com.hcc.ac.checks.CheckResult;
import com.hcc.ac.checks.ICheck;
import com.hcc.ac.checks.combat.AutoClickerCheck;
import com.hcc.event.InvokeEvent;
import com.hcc.event.PlayerSwingEvent;

import java.util.*;

/**
* We take pride in our AntiCheat
* @author Cubxity
*/
public class AntiCheat {
    private List<User> users = new ArrayList<>();
    private List<ICheck> checks = new ArrayList<>();
    private Timer timer = new Timer();

    /**
     * adds user to the ac system
     * @param user the user
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * start timers and stuff
     */
    public void init() {
        registerChecks();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onRun();
            }
        }, 0, 1000);
    }

    private void registerChecks() {
        addCheck(new AutoClickerCheck());
    }

    /**
     * executed every 1 second
     */
    private void onRun() {
        users.forEach(User::resetAPS);
        checks.forEach(check -> users.forEach(user -> {
            CheckResult result = check.check(user);
            if(result.getLevel() != CheckResult.Level.CLEAN){
                HCC.INSTANCE.sendMessage("AC: "+user.getPlayer()+" has been detected for "+result.getDetection()+" Level: "+result.getLevel()+" "+result.getDescription());
            }
        }));
    }

    /**
     * finds user in the system, if not found will add new and return it
     * @param player UUID of player
     * @return the user
     */
    public User getUser(UUID player) {
        for(User u : users)
            if(u.getPlayer().equals(player))
                return u;
        User u = new User(player);
        addUser(u);
        return u;
    }

    /**
     * registers the check to the system
     * @param check Implementation of check
     */
    public void addCheck(ICheck check){
        checks.add(check);
    }

    /**
     * @param event when a player swings
     */
    @InvokeEvent
    public void onSwing(PlayerSwingEvent event) {
        getUser(event.getPlayer()).onSwing();
    }

}
