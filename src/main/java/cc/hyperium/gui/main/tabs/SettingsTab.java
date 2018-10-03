package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Category;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.Settings;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.cosmetics.Deadmau5Cosmetic;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.gui.main.MainHyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    private final HyperiumOverlay general = new HyperiumOverlay("General");
    private final HyperiumOverlay integrations = new HyperiumOverlay("Integrations");
    private final HyperiumOverlay improvements = new HyperiumOverlay("Improvements");
    private final HyperiumOverlay cosmetics = new HyperiumOverlay("Cosmetics");
    private final HyperiumOverlay spotify = new HyperiumOverlay("Spotify");
    private final HyperiumOverlay misc = new HyperiumOverlay("Misc");
    private final HyperiumOverlay mods = new HyperiumOverlay("Modds ??? do we need ?? ");

    private final ColourOptions colourOptions = new ColourOptions();

    private final HashMap<Field, Consumer<Object>> callback = new HashMap<>();
    private final HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private GuiBlock block;
    private int y, w;


    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(general), Icons.SETTINGS.getResource(), "General", "General settings for Hyperium", "Click to configure", 0, 0));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(integrations), Icons.EXTENSION.getResource(), "Integrations", "Hyperium integrations", "Click to configure", 1, 0));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(improvements), Icons.TOOL.getResource(), "Improvements", "Improvements and bug fixes", "Click to configure", 2, 0));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(cosmetics), Icons.COSMETIC.getResource(), "Cosmetics", "Bling out your Minecraft Avatar", "Click to configure", 0, 1));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setTab(MainHyperiumMainGui.INSTANCE.getModsTab()), Icons.EXTENSION.getResource(), "Mods", "Hyperium mod settings", "Click to configure", 1, 1));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(misc), Icons.MISC.getResource(), "Miscellaneous", "Other Hyperium Settings", "Click to configure", 2, 1));

        items.add(new SettingItem(() -> MainHyperiumMainGui.INSTANCE.setOverlay(colourOptions), Icons.TOOL.getResource(), "GUI Options", "Accent Colours, etc.", "Click to configure", 0, 2));

        try {
            Field earsField = Settings.class.getField("EARS_STATE");
            callback.put(earsField, o -> {
                boolean yes = ((String) o).equalsIgnoreCase("YES");
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null) {
                    GeneralChatHandler.instance().sendMessage("Error: Could not update cosmetic state because your purchase profile is not loaded.");
                    return;
                }
                JsonHolder purchaseSettings = self.getPurchaseSettings();
                if (!purchaseSettings.has("deadmau5_cosmetic")) {
                    purchaseSettings.put("deadmau5_cosmetic", new JsonHolder());
                }
                purchaseSettings.optJSONObject("deadmau5_cosmetic").put("enabled", yes);
                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("ears", yes)));
                }
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
            customStates.put(Settings.class.getField("SHOW_BUTT"), () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.BUTT)) {
                    return new String[]{
                            "YES",
                            "NO"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });
            callback.put(Settings.class.getField("SHOW_BUTT"),o -> {
                boolean yes = !((String) o).equalsIgnoreCase("YES");
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if(self !=null) {
                    JsonHolder purchaseSettings = self.getPurchaseSettings();
                    purchaseSettings.put("butt",new JsonHolder());
                    purchaseSettings.optJSONObject("butt").put("disabled",yes);
                }
                NettyClient client = NettyClient.getClient();
                if (client != null) {
                    JsonHolder put = new JsonHolder().put("internal", true).put("butt_disabled", yes);
                    client.write(ServerCrossDataPacket.build(put));
                }

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

            Field show_wings_string = Settings.class.getField("SHOW_WINGS");
            customStates.put(show_wings_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.WING_COSMETIC)) {
                    return new String[]{
                            "ON",
                            "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });

            callback.put(show_wings_string, o -> {
                try {
                    Settings.SHOW_WINGS = String.valueOf(o);
                } catch (Exception ignored) {

                }
            });

            Field show_dragonhead_string = Settings.class.getField("SHOW_DRAGON_HEAD");
            customStates.put(show_dragonhead_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.DRAGON_HEAD)) {
                    return new String[]{
                            "ON",
                            "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });
            callback.put(show_dragonhead_string, o -> {
                try {
                    Settings.SHOW_DRAGON_HEAD = String.valueOf(o);
                } catch (Exception ignored) {

                }
            });

            callback.put(Settings.class.getField("MAX_WORLD_PARTICLES_STRING"), o -> {
                try {
                    Settings.MAX_WORLD_PARTICLES_INT = Integer.valueOf(o.toString());
                } catch (Exception ignored) {

                }
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
            callback.put(Settings.class.getField("WINGS_SCALE"), o -> {
                if (PurchaseApi.getInstance() == null || PurchaseApi.getInstance().getSelf() == null || PurchaseApi.getInstance().getSelf().getPurchaseSettings() == null) {
                    return;
                }
                Float o1 = (Float) o;
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null) {
                    GeneralChatHandler.instance().sendMessage("Error: Could not update cosmetic state because your purchase profile is not loaded.");
                    return;
                }
                JsonHolder purchaseSettings = self.getPurchaseSettings();
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
        for (Object o : MainHyperiumMainGui.INSTANCE.getSettingsObjects()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                Consumer<Object> objectConsumer = callback.get(f);
                if (ts != null) {
                    if (ts.mods())
                        continue;
                    getCategory(ts.category()).addToggle(ts.name(), f, objectConsumer, ts.enabled(), o);
                } else if (ss != null) {
                    if (ss.mods())
                        continue;
                    try {
                        Supplier<String[]> supplier = customStates.get(f);
                        Supplier<String[]> supplier1 = supplier != null ? supplier : ss::items;
                        String current = String.valueOf(f.get(o));
                        if (!ArrayUtils.contains(supplier1.get(), current))
                            current = supplier1.get()[0];
                        getCategory(ss.category()).getComponents().add(new OverlaySelector<>(ss.name(), current, si -> {
                            if (objectConsumer != null)
                                objectConsumer.accept(si);
                            try {
                                f.set(o, si);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }, supplier1, ss.enabled()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (sliderSetting != null) {
                    if (sliderSetting.mods())
                        continue;
                    try {
                        Double value = Double.valueOf(f.get(o).toString());
                        getCategory(sliderSetting.category()).getComponents().add(new OverlaySlider(sliderSetting.name(), sliderSetting.min(), sliderSetting.max(),
                                value.floatValue(), aFloat -> {
                            if (objectConsumer != null)
                                objectConsumer.accept(aFloat);
                            try {
                                f.set(o, aFloat);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }, sliderSetting.round(), sliderSetting.enabled()));

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Settings.save();
    }


    private HyperiumOverlay getCategory(Category settingsCategory) {
        switch (settingsCategory) {
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

            case MODS:
                return mods;
            case MISC:
                return misc;
        }
        return general;
    }

    @Override
    public void drawTabIcon() {
        Icons.SETTINGS.bind();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
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
    public String getTitle() {
        return "Settings";
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
    }
}
