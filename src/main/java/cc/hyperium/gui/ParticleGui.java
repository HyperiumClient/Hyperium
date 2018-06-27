package cc.hyperium.gui;

import cc.hyperium.gui.carousel.CarouselItem;
import cc.hyperium.gui.carousel.PurchaseCarousel;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class ParticleGui extends HyperiumGui {

    private PurchaseCarousel particleType;

    private PurchaseCarousel particleAnimation;

    public ParticleGui() {
        //TODO quick maths to find out current index
        particleType = PurchaseCarousel.create(1, new CarouselItem("type 1", false), new CarouselItem("type 2", false), new CarouselItem("type 3", false));
        particleAnimation = PurchaseCarousel.create(1, new CarouselItem("anim 1", false), new CarouselItem("anim 2", false), new CarouselItem("anim 3", false));

    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        particleType.render(current.getScaledWidth() / 5, current.getScaledHeight() / 2);

        particleAnimation.render(current.getScaledWidth() *4 / 5 , current.getScaledHeight() / 2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        particleType.mouseClicked(0,0);
    }
}
