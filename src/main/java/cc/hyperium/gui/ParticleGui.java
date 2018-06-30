package cc.hyperium.gui;

import cc.hyperium.gui.carousel.CarouselItem;
import cc.hyperium.gui.carousel.PurchaseCarousel;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class ParticleGui extends HyperiumGui {

    private HyperiumOverlay overlay;

    private PurchaseCarousel particleType;

    private PurchaseCarousel particleAnimation;

    public ParticleGui() {
        //TODO quick maths to find out current index
        particleType = PurchaseCarousel.create(1, new CarouselItem("type 1", false), new CarouselItem("type 2", false), new CarouselItem("type 3", false));
        particleAnimation = PurchaseCarousel.create(1, new CarouselItem("anim 1", false), new CarouselItem("anim 2", false), new CarouselItem("anim 3", false));
        // overlay = new HyperiumOverlay();
        // overlay.getComponents().add(new OverlayButton("Hello", () -> System.out.println("Hello!")));
    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        particleType.render(current.getScaledWidth() / 5, current.getScaledHeight() / 2, mouseX, mouseY);

        particleAnimation.render(current.getScaledWidth() * 4 / 5, current.getScaledHeight() / 2, mouseX, mouseY);

        if (overlay != null)
            overlay.render(mouseX, mouseY, width, height);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (overlay == null) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            particleType.mouseClicked(mouseX, mouseY, width / 5);
            particleAnimation.mouseClicked(mouseX, mouseY, width * 4 / 5);
        } else {
            int x = width / 6 * 2;
            int y = height / 4;
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y - 16 && mouseY <= y) {
                overlay = null;
            } else
                overlay.mouseClicked();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (overlay != null && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            overlay.reset();
            overlay = null;
        } else
            super.handleKeyboardInput();
    }

    @Override
    public void handleMouseInput() {
        if(overlay != null)
            overlay.handleMouseInput();
    }
}