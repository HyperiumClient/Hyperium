package cc.hyperium.gui.carousel;

import java.util.function.Consumer;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class CarouselItem {

    private boolean purchased;
    private String name;
    private Consumer<CarouselItem> onPurchase;
    private Consumer<CarouselItem> onClick;
    private Consumer<CarouselItem> onSettingsClick;

    public CarouselItem(String name, boolean purchased) {
        this(name, purchased, carouselItem -> {
        }, carouselItem -> {
        }, carouselItem -> {
        });
    }

    public CarouselItem(String name, boolean purchased, Consumer<CarouselItem> onPurchase, Consumer<CarouselItem> onClick, Consumer<CarouselItem> onSettingsClick) {
        this.purchased = purchased;
        this.name = name;

        this.onPurchase = onPurchase;
        this.onClick = onClick;
        this.onSettingsClick = onSettingsClick;
    }

    public Consumer<CarouselItem> getOnPurchase() {
        return onPurchase;
    }

    public Consumer<CarouselItem> getOnClick() {
        return onClick;
    }

    public Consumer<CarouselItem> getOnSettingsClick() {
        return onSettingsClick;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public String getName() {
        return name;
    }

    public void mouseClicked(int x, int y) {

    }
}
