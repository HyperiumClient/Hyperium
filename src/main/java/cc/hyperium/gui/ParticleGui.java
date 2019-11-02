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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.PurchaseLoadEvent;
import cc.hyperium.gui.carousel.CarouselItem;
import cc.hyperium.gui.carousel.PurchaseCarousel;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import cc.hyperium.handlers.handlers.particle.EnumParticleType;
import cc.hyperium.handlers.handlers.particle.ParticleAuraHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class ParticleGui extends HyperiumGui implements GuiYesNoCallback {

    private HashMap<Integer, Runnable> ids = new HashMap<>();
    private int purchaseIds;
    private HyperiumOverlay overlay;
    private PurchaseCarousel particleType;
    private PurchaseCarousel particleAnimation;
    private int credits;
    private GuiBlock previewBlock;
    private boolean queueBuild;

    @Override
    public void confirmClicked(boolean result, int id) {
        super.confirmClicked(result, id);

        if (result) {
            Runnable runnable = ids.get(id);
            if (runnable != null) runnable.run();
        }

        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }

    @Override
    public void initGui() {
        EventBus.INSTANCE.register(this);
        super.initGui();
        queueBuild = true;
    }

    @InvokeEvent
    public void loadPurchaseEvent(PurchaseLoadEvent event) {
        if (event.getSelf()) {
            queueBuild = true;
        }
    }

    private void rebuild() {
        EnumParticleType[] values = EnumParticleType.values();

        int length = values.length;
        HyperiumPurchase self = PurchaseApi.getInstance().getSelf();

        if (self == null) {
            queueBuild = true;
            return;
        }

        JsonHolder purchaseSettings = self.getPurchaseSettings();
        if (!purchaseSettings.has("particle")) purchaseSettings.put("particle", new JsonHolder());
        JsonHolder particle = purchaseSettings.optJSONObject("particle");

        CarouselItem[] particles = new CarouselItem[length];
        for (int i = 0; i < length; i++) {
            EnumParticleType value = values[i];
            final boolean flag = self.hasPurchased("PARTICLE_" + value.name());

            particles[i] = new CarouselItem(value.getName(), flag, false, carouselItem -> {
                if (!flag) {
                    if (credits < 300) {
                        GeneralChatHandler.instance().sendMessage(I18n.format("message.insufficientcredits"));
                        return;
                    }

                    int i4 = ++purchaseIds;
                    GuiYesNo gui = new GuiYesNo(this, I18n.format("message.purchase", value.getName()), "", i4);
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(gui);

                    ids.put(i4, () -> {
                        GeneralChatHandler.instance().sendMessage(I18n.format("message.attemptingpurchase", value.getName()));
                        NettyClient client = NettyClient.getClient();
                        if (client != null) {
                            client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase",
                                true).put("value", "PARTICLE_" + value.name())));
                        }
                    });
                } else {
                    GeneralChatHandler.instance().sendMessage(I18n.format("message.alreadypurchased", value.getName()));
                }
            }, carouselItem -> {
                if (!flag) return;

                overlay = new HyperiumOverlay("Particles");
                String s = particle.optBoolean("rgb") ? "RGB" : particle.optBoolean("chroma") ? "CHROMA" : "DEFAULT";

                overlay.getComponents().add(new OverlaySelector<>("Color Type", s, s1 -> {
                    if (s1.equals("DEFAULT")) {
                        particle.put("chroma", false);
                        particle.put("rgb", false);
                    } else if (s1.equals("CHROMA")) {
                        particle.put("chroma", true);
                        particle.put("rgb", false);
                    } else if (s1.equalsIgnoreCase("RGB")) {
                        particle.put("rgb", true);
                        particle.put("chroma", false);
                    }

                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("color_type", s1)));
                    }
                    //Rebuild auto called on purchase update
                }, () -> new String[]{"DEFAULT", "RGB", "CHROMA"}));

                overlay.getComponents().add(new OverlaySlider(I18n.format("gui.cosmetics.red"), 0, 255, particle.optInt("red", 255), aFloat -> {
                    particle.put("red", aFloat);
                    EventBus.INSTANCE.post(new PurchaseLoadEvent(Objects.requireNonNull(UUIDUtil.getClientUUID()), self, true));
                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("red", aFloat.intValue())));
                    }
                }, true));

                overlay.getComponents().add(new OverlaySlider(I18n.format("gui.cosmetics.green"), 0, 255, particle.optInt("green", 255), aFloat -> {
                    particle.put("green", aFloat);
                    EventBus.INSTANCE.post(new PurchaseLoadEvent(Objects.requireNonNull(UUIDUtil.getClientUUID()), self, true));
                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("green", aFloat.intValue())));
                    }
                }, true));

                overlay.getComponents().add(new OverlaySlider(I18n.format("gui.cosmetics.blue"), 0, 255, particle.optInt("blue", 255), aFloat -> {
                    particle.put("blue", aFloat);
                    EventBus.INSTANCE.post(new PurchaseLoadEvent(Objects.requireNonNull(UUIDUtil.getClientUUID()), self, true));
                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("blue", aFloat.intValue())));
                    }
                }, true));

                overlay.getComponents().add(new OverlaySlider(I18n.format("gui.cosmetics.maxage"), 2, 100, particle.optInt("max_age", 10), aFloat -> {
                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("max_age", aFloat.intValue())));
                    }
                }, true));
            }, carouselItem -> {
                particle.put("type", value.name());
                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("active_type", value.name())));
                }

                Arrays.stream(particles).forEach(item -> item.setActive(false));
                carouselItem.setActive(true);
            });
        }

        int spot = 0;
        try {
            spot = EnumParticleType.valueOf(particle.optString("type")).ordinal();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (particles[spot].isPurchased()) particles[spot].setPurchased(true);
        particleType = PurchaseCarousel.create(spot, particles);

        ParticleAuraHandler particleAuraHandler = Hyperium.INSTANCE.getHandlers().getParticleAuraHandler();
        HashMap<String, AbstractAnimation> animations = particleAuraHandler.getAnimations();
        String[] keys = {"Double Twirl", "Tornado", "Double Helix", "Triple Twirl", "Quad Twirl", "Static Trail", "Explode", "Vortex of doom"};
        CarouselItem[] animationItems = new CarouselItem[animations.size()];
        int c = 0;
        int g = 0;
        for (String s : keys) {
            boolean flag = self.hasPurchased("ANIMATION_" + s.replace(" ", "_".toUpperCase()));
            boolean flag1 = particle.optString("particle_animation").equalsIgnoreCase(s);
            if (flag1) g = c;

            animationItems[c] = new CarouselItem(s, flag, flag1, carouselItem -> {
                if (!flag) {
                    int i4 = ++purchaseIds;
                    GuiYesNo gui = new GuiYesNo(this, I18n.format("message.purchase", s), "", i4);
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(gui);

                    ids.put(i4, () -> {
                        GeneralChatHandler.instance().sendMessage(I18n.format("message.attemptingpurchase", s));
                        NettyClient client = NettyClient.getClient();
                        if (client != null) {
                            client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("cosmetic_purchase",
                                true).put("value", "ANIMATION_" + s.replace(" ", "_").toUpperCase())));
                        }
                    });
                } else {
                    GeneralChatHandler.instance().sendMessage(I18n.format("message.alreadypurchased", s));
                }
            }, carouselItem -> {
                //No settings as of now

            }, carouselItem -> {
                particle.put("particle_animation", s);
                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_update", true).put("particle_animation", s)));
                }

                Arrays.stream(animationItems).forEach(animationItem -> animationItem.setActive(false));
                carouselItem.setActive(true);
            });

            c++;
        }

        particleAnimation = PurchaseCarousel.create(g, animationItems);
    }

    @Override
    protected void pack() {
        reg("RESET", new GuiButton(nextId(), 1, 1, I18n.format("gui.cosmetics.disableparticles")), button -> {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("particle_reset", true)));
            }
        }, button -> {
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (queueBuild) {
            queueBuild = false;
            rebuild();
        }

        GlStateManager.scale(2.0, 2.0, 2.0);
        HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
        if (self != null) credits = self.getResponse().optInt("remaining_credits");

        String s = I18n.format("gui.cosmetics.credits") + ": " + credits;
        fontRendererObj.drawString(s, (ResolutionUtil.current().getScaledWidth() >> 2) - (fontRendererObj.getStringWidth(s) >> 1),
            15, Color.MAGENTA.getRGB(), true);

        GlStateManager.scale(.5, .5, .5);
        s = I18n.format("gui.cosmetics.line1");
        fontRendererObj.drawString(s, (ResolutionUtil.current().getScaledWidth() >> 1) - (fontRendererObj.getStringWidth(s) >> 1),
            50, Color.MAGENTA.getRGB(), true);
        s = I18n.format("gui.cosmetics.line2");
        fontRendererObj.drawString(s, (ResolutionUtil.current().getScaledWidth() >> 1) - (fontRendererObj.getStringWidth(s) >> 1),
            61, Color.MAGENTA.getRGB(), true);

        s = I18n.format("gui.cosmetics.preview");
        GlStateManager.scale(2.0, 2.0, 2.0);
        int stringWidth = fontRendererObj.getStringWidth(s);
        int x1 = ResolutionUtil.current().getScaledWidth() / 4 - stringWidth / 4;
        int y1 = ResolutionUtil.current().getScaledHeight() / 4;
        fontRendererObj.drawString(s, x1, y1, Color.MAGENTA.getRGB(), true);
        GlStateManager.scale(.5, .5, .5);
        previewBlock = new GuiBlock(x1 * 2, x1 * 2 + stringWidth * 2, y1 * 2, y1 * 2 + 20);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        particleType.render(current.getScaledWidth() / 5, current.getScaledHeight() / 2, mouseX, mouseY);
        particleAnimation.render(current.getScaledWidth() * 4 / 5, current.getScaledHeight() / 2, mouseX, mouseY);

        if (overlay != null) overlay.render(mouseX, mouseY, width, height);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (previewBlock != null && previewBlock.isMouseOver(mouseX, mouseY) && overlay == null) {
            mc.displayGuiScreen(null);
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
            EnumParticleType type = null;

            for (EnumParticleType enumParticleType : EnumParticleType.values()) {
                if (enumParticleType.getName().equalsIgnoreCase(particleType.getCurrent().getName())) {
                    type = enumParticleType;
                }
            }

            if (type == null) {
                GeneralChatHandler.instance().sendMessage(I18n.format("message.invalidparticle"));
                return;
            }

            HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
            String name = particleAnimation.getCurrent().getName();

            if (Hyperium.INSTANCE.getHandlers().getParticleAuraHandler().getAnimations().get(name) == null) {
                GeneralChatHandler.instance().sendMessage(I18n.format("message.invalidanimation"));
                return;
            }

            JsonHolder oldsettings = new JsonHolder(self.getPurchaseSettings().getObject().toString());
            JsonHolder purchaseSettings = self.getPurchaseSettings();
            if (purchaseSettings.has("particle")) purchaseSettings.put("particle", new JsonHolder());

            JsonHolder holder = purchaseSettings.optJSONObject("particle");
            holder.put("type", type.name());
            holder.put("particle_animation", name);
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                self.getPurchaseSettings().merge(oldsettings, true);
                self.refreshCachedSettings();
                EventBus.INSTANCE.post(new PurchaseLoadEvent(Objects.requireNonNull(UUIDUtil.getClientUUID()), self, true));
                new ParticleGui().show();
            });

            EventBus.INSTANCE.post(new PurchaseLoadEvent(Objects.requireNonNull(UUIDUtil.getClientUUID()), self, true));
            return;
        }
        if (overlay == null) {
            particleType.mouseClicked(mouseX, mouseY, width / 5);
            particleAnimation.mouseClicked(mouseX, mouseY, width * 4 / 5);
        } else {
            int x = width / 6 * 2;
            int y = height / 4;
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y - 16 && mouseY <= y) {
                overlay = null;
            } else {
                overlay.mouseClicked();
            }
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (overlay != null && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            overlay.reset();
            overlay = null;
        } else {
            super.handleKeyboardInput();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (overlay != null) overlay.handleMouseInput();
    }
}
