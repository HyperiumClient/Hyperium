/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.KeyBindPressEvent;


public class HyperiumBind {
    private String name;
    private int key;

    private boolean pressed = false;
    private boolean activated = false;

    public HyperiumBind(String name, int key){
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
            EventBus.INSTANCE.post(new KeyBindPressEvent(key));
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
