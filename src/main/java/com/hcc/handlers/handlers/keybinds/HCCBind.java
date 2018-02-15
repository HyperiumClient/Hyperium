/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.handlers.handlers.keybinds;

import com.hcc.event.EventBus;
import com.hcc.event.KeyBindDisableEvent;
import com.hcc.event.KeyBindEnableEvent;

public class HCCBind {
    private String name;
    private int key;

    private boolean pressed = false;
    private boolean activated = false;

    public HCCBind(String name, int key){
        this.name = name;
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public int getKey(){
        return key;
    }

    public void setKey(int key){
        this.key = key;
    }

    public void onPress(){
        if(!pressed){
            if(!activated){
                System.out.println("[KEYBINDS] " + name + "keybind enabled!");
                EventBus.INSTANCE.post(new KeyBindEnableEvent(key));
            } else{
                System.out.println("[KEYBINDS] " + name + "keybind disabled!");
                EventBus.INSTANCE.post(new KeyBindDisableEvent(key));
            }
            activated = !activated;
        }
        pressed = !pressed;
    }

    public boolean isPressed(){
        return pressed;
    }

    public boolean isActivated(){
        return activated;
    }
}
