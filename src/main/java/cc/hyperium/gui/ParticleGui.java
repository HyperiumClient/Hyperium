package cc.hyperium.gui;

import cc.hyperium.gui.carousel.CarouselItem;
import cc.hyperium.gui.carousel.PurchaseCarousel;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class ParticleGui extends HyperiumGui {

    private PurchaseCarousel particleType;

    public ParticleGui() {
        //TODO quick maths to find out current index
        particleType = PurchaseCarousel.create(1, new CarouselItem("index 1", false), new CarouselItem("index 2", false), new CarouselItem("index 3", false));
    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        particleType.render(current.getScaledWidth() / 2, current.getScaledHeight() / 2);
    }
}
