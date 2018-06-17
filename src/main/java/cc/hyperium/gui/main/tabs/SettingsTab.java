package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Category;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.Settings;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.cosmetics.Deadmau5Cosmetic;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.cosmetics.wings.WingsCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PurchaseLoadEvent;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.mods.glintcolorizer.gui.GlintColorizerSettings;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Mouse;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    private final HyperiumOverlay general = new HyperiumOverlay();
    private final HyperiumOverlay integrations = new HyperiumOverlay();
    private final HyperiumOverlay improvements = new HyperiumOverlay();
    private final HyperiumOverlay cosmetics = new HyperiumOverlay();
    private final HyperiumOverlay spotify = new HyperiumOverlay();
    private final HyperiumOverlay animations = new HyperiumOverlay();
    private final HyperiumOverlay misc = new HyperiumOverlay();
    private final GlintColorizerSettings glintcolorizer = new GlintColorizerSettings();

    private final HyperiumOverlay wings = new HyperiumOverlay();
    private final HashMap<Field, Consumer<Object>> callback = new HashMap<>();
    private final HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    boolean loadedSelf = false;
    private int offsetY = 0;
    private GuiBlock block;
    private int y, w;

    {
        try {
            Field earsField = Settings.class.getField("EARS_STATE");
            callback.put(earsField, o -> {
                boolean yes = ((String) o).equalsIgnoreCase("YES");
                JsonHolder purchaseSettings = PurchaseApi.getInstance().getSelf().getPurchaseSettings();
                if (!purchaseSettings.has("deadmau5_cosmetic")) {
                    purchaseSettings.put("deadmau5_cosmetic", new JsonHolder());
                }
                purchaseSettings.optJSONObject("deadmau5_cosmetic").put("enabled", yes);
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("ears", yes)));
            });
            customStates.put(earsField, () -> {
                Hyperium instance = Hyperium.INSTANCE;
                if (instance != null) {
                    HyperiumCosmetics cosmetics1 = instance.getCosmetics();
                    if (cosmetics1 != null) {
                        Deadmau5Cosmetic deadmau5Cosmetic = cosmetics1.getDeadmau5Cosmetic();
                        if (deadmau5Cosmetic != null) {
                            if (deadmau5Cosmetic.isSelfUnlocked()) {
                                return new String[]{"YES", "NO"};
                            }
                        }
                    }
                }
                return new String[]{"NOT PURCHASED"};
            });
            Field max_particle_string = Settings.class.getField("MAX_PARTICLE_STRING");
            customStates.put(max_particle_string, () -> {
                ParticleOverlay overlay = ParticleOverlay.getOverlay();
                if (overlay.purchased()) {
                    return new String[]{
                            "25",
                            "50",
                            "100",
                            "150",
                            "200",
                            "250",
                            "300"};
                }
                return new String[]{"NOT PURCHASED"};
            });
            callback.put(max_particle_string, o -> {
                try {
                    Settings.MAX_PARTICLES = Integer.valueOf(o.toString());
                } catch (Exception ignored) {

                }
            });
            callback.put(Settings.class.getField("HEAD_SCALE_FACTOR_STRING"), o -> {
                try {
                    Settings.HEAD_SCALE_FACTOR = Double.valueOf(o.toString());
                } catch (Exception ignored) {

                }
            });
            callback.put(Settings.class.getField("wingsSELECTED"), o -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null)
                    return;
                JsonHolder purchaseSettings = self.getPurchaseSettings();
                if (!purchaseSettings.has("wings"))
                    purchaseSettings.put("wings", new JsonHolder());
                purchaseSettings.optJSONObject("wings").put("type", o.toString());
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings", o.toString())));
            });

            Field flip_type_string = Settings.class.getField("FLIP_TYPE_STRING");
            customStates.put(flip_type_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null || !self.hasPurchased(EnumPurchaseType.FLIP_COSMETIC))
                    return new String[]{"NOT PURCHASED"};
                else return new String[]{"FLIP", "ROTATE"};
            });
            callback.put(flip_type_string, o -> {
                String s = o.toString();
                if (s.equalsIgnoreCase("FLIP")) {
                    Settings.flipType = 1;
                } else if (s.equalsIgnoreCase("ROTATE")) {
                    Settings.flipType = 2;
                }
            });
            callback.put(Settings.class.getField("SHOW_DRAGON_HEAD"), o -> {
                boolean yes = (o.toString()).equalsIgnoreCase("true");
                JsonHolder purchaseSettings = PurchaseApi.getInstance().getSelf().getPurchaseSettings();
                if (!purchaseSettings.has("dragon"))
                    purchaseSettings.put("dragon", new JsonHolder());
                purchaseSettings.optJSONObject("dragon").put("disabled", !yes);
                NettyClient client = NettyClient.getClient();

                if (client != null)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("dragon_head", yes)));

            });
            callback.put(Settings.class.getField("SHOW_WINGS"), o -> {
                boolean yes = (o.toString()).equalsIgnoreCase("true");
                JsonHolder purchaseSettings = PurchaseApi.getInstance().getSelf().getPurchaseSettings();
                if (!purchaseSettings.has("wings"))
                    purchaseSettings.put("wings", new JsonHolder());
                purchaseSettings.optJSONObject("wings").put("disabled", !yes);
                NettyClient client = NettyClient.getClient();
                if (client != null)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_toggle", yes)));
            });
            callback.put(Settings.class.getField("WINGS_SCALE"), o -> {
                Float o1 = (Float) o;
                JsonHolder purchaseSettings = PurchaseApi.getInstance().getSelf().getPurchaseSettings();
                if (!purchaseSettings.has("wings"))
                    purchaseSettings.put("wings", new JsonHolder());
                purchaseSettings.optJSONObject("wings").put("scale", o1);
                Settings.WINGS_SCALE = o1;
                NettyClient client = NettyClient.getClient();
                if (client != null)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_scale", o1.doubleValue())));

            });


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        for (Field f : Settings.class.getFields()) {
            ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
            SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
            SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
            Consumer<Object> objectConsumer = callback.get(f);
            if (ts != null) {
                getCategory(ts.category()).addToggle(ts.name(), f, objectConsumer);
            } else if (ss != null) {
                try {
                    Supplier<String[]> supplier = customStates.get(f);
                    Supplier<String[]> supplier1 = supplier != null ? supplier : ss::items;
                    String current = String.valueOf(f.get(null));
                    if (!ArrayUtils.contains(supplier1.get(), current))
                        current = supplier1.get()[0];
                    getCategory(ss.category()).getComponents().add(new OverlaySelector<>(ss.name(), current, si -> {
                        if (objectConsumer != null)
                            objectConsumer.accept(si);
                        try {
                            f.set(null, si);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }, supplier1));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (sliderSetting != null) {

                try {
                    Double value = Double.valueOf(f.get(null).toString());
                    getCategory(sliderSetting.category()).getComponents().add(new OverlaySlider(sliderSetting.name(), sliderSetting.min(), sliderSetting.max(),
                            value.floatValue(), aFloat -> {
                        if (objectConsumer != null)
                            objectConsumer.accept(aFloat);
                        try {
                            f.set(null, aFloat);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Settings.save();
    }

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(general), Icons.SETTINGS.getResource(), "General", "General settings for Hyperium", "Click to configure", 0, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(integrations), Icons.EXTENSION.getResource(), "Integrations", "Hyperium integrations", "Click to configure", 1, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(improvements), Icons.TOOL.getResource(), "Improvements", "Improvements and bug fixes", "Click to configure", 2, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(cosmetics), Icons.COSMETIC.getResource(), "Cosmetics", "Bling out your Minecraft Avatar", "Click to configure", 0, 1));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(animations), Icons.COSMETIC.getResource(), "Animations", "Adjust the Minecraft Animations", "Click to configure", 2, 1));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(spotify), Icons.SPOTIFY.getResource(), "Spotify", "Hyperium Spotify Settings", "Click to configure", 1, 1));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(glintcolorizer), Icons.EXTENSION.getResource(), "GlintColorizer", "GlintColorizer settings", "Click to configure", 0, 2));
        //TODO fix this method being async
        WingsCosmetic wingsCosmetic = Hyperium.INSTANCE.getCosmetics().getWingsCosmetic();
        if (wingsCosmetic.isSelfUnlocked()) {
            loadedSelf = true;
            items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(wings), Icons.COSMETIC.getResource(), "Wings", "Hyperium wings Settings", "Click to configure", 1, 2));

            items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(misc), Icons.MISC.getResource(), "Miscellaneous", "Other Hyperium Settings", "Click to configure", 2, 2));
        } else {
          items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(misc), Icons.MISC.getResource(), "Miscellaneous", "Other Hyperium Settings", "Click to configure", 1, 2));
        }
    }

    @InvokeEvent
    public void purchaseLoad(PurchaseLoadEvent event) {
        if (loadedSelf) return;
        if (event.getSelf()) {
            if (event.getPurchase().hasPurchased(EnumPurchaseType.WING_COSMETIC)) {
                items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(wings), Icons.COSMETIC.getResource(), "Wings", "Hyperium wings Settings", "Click to configure", 0, 2));
            }
        }
    }

    private HyperiumOverlay getCategory(Category category) {
        switch (category) {
            case GENERAL:
                return general;
            case IMPROVEMENTS:
                return improvements;
            case INTEGRATIONS:
                return integrations;
            case COSMETICS:
                return cosmetics;
            case SPOTIFY:
                return spotify;
            case WINGS:
                return wings;
            case ANIMATIONS:
                return animations;
            case MISC:
                return misc;
        }
        return general;
    }

    @Override
    public void drawTabIcon() {
        Icons.SETTINGS.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY + offsetY, containerWidth, containerHeight);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (HyperiumMainGui.INSTANCE.getOverlay() != null) return;
        int i = Mouse.getEventDWheel();

        if (i < 0)
            offsetY -= 5;
        else if (i > 0)
            offsetY += 5;
    }
}
