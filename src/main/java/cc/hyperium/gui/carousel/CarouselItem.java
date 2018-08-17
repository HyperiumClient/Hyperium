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

    public CarouselItem(String name, boolean purchased) {
        this(name, purchased, false, carouselItem -> {
        }, carouselItem -> {
        }, carouselItem -> {

        });
    }

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

    public void setPurchased(boolean b) {
        this.purchased = b;
    }

    public String getName() {
        return name;
    }
}
