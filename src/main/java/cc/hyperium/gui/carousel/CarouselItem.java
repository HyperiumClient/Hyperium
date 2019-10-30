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

package cc.hyperium.gui.carousel;

import java.util.function.Consumer;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class CarouselItem {

    private boolean purchased;
    private boolean active;
    private String name;
    private Consumer<CarouselItem> onPurchase;
    private Consumer<CarouselItem> onSettingsClick;
    private Consumer<CarouselItem> onActivate;

    public CarouselItem(String name, boolean purchased, boolean active, Consumer<CarouselItem> onPurchase, Consumer<CarouselItem> onSettingsClick, Consumer<CarouselItem> onActivate) {
        this.purchased = purchased;
        this.name = name;
        this.active = active;

        this.onPurchase = onPurchase;
        this.onSettingsClick = onSettingsClick;
        this.onActivate = onActivate;
    }

    public Consumer<CarouselItem> getOnActivate() {
        return onActivate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Consumer<CarouselItem> getOnPurchase() {
        return onPurchase;
    }

    public Consumer<CarouselItem> getOnSettingsClick() {
        return onSettingsClick;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }
}
