package cc.hyperium.gui.main.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.gui.GuiEditCrosshair;
import cc.hyperium.config.Category;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.Settings;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlayLabel;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.mods.blockoverlay.BlockOverlayGui;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;
import cc.hyperium.mods.glintcolorizer.gui.GlintColorizerSettings;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.guis.LevelHeadGui;
import cc.hyperium.mods.motionblur.MotionBlurMod;
import cc.hyperium.mods.spotify.SpotifyGui;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.MessageOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;

public class ModsTab extends AbstractTab {
    private final HyperiumOverlay autotip = new HyperiumOverlay("Autotip");
    private final HyperiumOverlay levelhead = new HyperiumOverlay("Levelhead");
    private final HyperiumOverlay vanilla = new HyperiumOverlay("Vanilla Enhancements");
    private final HyperiumOverlay motionblur = new HyperiumOverlay("Motion Blur");
    private final HyperiumOverlay chromahud = new HyperiumOverlay("ChromaHUD");
    private final HyperiumOverlay animations = new HyperiumOverlay("Animations");
    private final HyperiumOverlay autoGG = new HyperiumOverlay("AutoGG");
    private final HyperiumOverlay autoFriend = new HyperiumOverlay("AutoFriend");
    private final HyperiumOverlay spotify = new HyperiumOverlay("Spotify");

    private final GlintColorizerSettings glintcolorizer = new GlintColorizerSettings();

    private final HashMap<Field, Consumer<Object>> callback = new HashMap<>();
    private final HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private GuiBlock block;
    private int y, w;

    public ModsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(autotip), Icons.SETTINGS.getResource(), "Autotip", "Autotip Settings \n /autotip", "Click to configure", 0, 0));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(levelhead), Icons.SETTINGS.getResource(), "Levelhead", "Levelhead Settings \n /levelhead", "Click to configure", 1, 0));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(vanilla), Icons.SETTINGS.getResource(), "Vanilla Enhancements", "Vanilla Enhancements", "Click to configure", 2, 0));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(motionblur), Icons.SETTINGS.getResource(), "Motion Blur", "Motion Blur Settings \n /motionblur", "Click to configure", 0, 1));
        items.add(new SettingItem(() -> {
            if (Minecraft.getMinecraft().thePlayer != null)
                Minecraft.getMinecraft().displayGuiScreen(new BlockOverlayGui(Hyperium.INSTANCE.getModIntegration().getBlockOverlay()));
        }, Icons.SETTINGS.getResource(), "Block Overlay", "Block overlay settings \n /blockoverlay ", "Click to configure", 1, 1));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(chromahud), Icons.SETTINGS.getResource(), "ChromaHUD", "ChromaHUD settings \n /chromahud", "Click to configure", 2, 1));
        items.add(new SettingItem(() -> {
            if (Minecraft.getMinecraft().thePlayer != null)
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenKeystrokes(Hyperium.INSTANCE.getModIntegration().getKeystrokesMod()));
        }, Icons.SETTINGS.getResource(), "Keystrokes", "Keystrokes settings \n /keystrokesmod", "Click to configure", 0, 2));
        items.add(new SettingItem(() -> {
            if (Minecraft.getMinecraft().thePlayer != null)
                Minecraft.getMinecraft().displayGuiScreen(new ToggleChatMainGui(Hyperium.INSTANCE.getModIntegration().getToggleChat(), 0));
        }, Icons.SETTINGS.getResource(), "Toggle chat", "Toggle chat settings \n /tc", "Click to configure", 1, 2));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(glintcolorizer), Icons.EXTENSION.getResource(), "GlintColorizer", "GlintColorizer settings", "Click to configure", 2, 2));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(animations), Icons.COSMETIC.getResource(), "1.7 Animations", "Adjust the Minecraft Animations", "Click to configure", 0, 3));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(spotify), Icons.SETTINGS.getResource(), "Spotify Settings", "Adjust Spotify integration settings", "Click to configure", 1, 3));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(autoGG), Icons.SETTINGS.getResource(), "AutoGG", "AutoGG \n /autogg", "Click to configure", 2, 3));
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(autoFriend), Icons.SETTINGS.getResource(), "AutoFriend", "AutoFriend automatically accepts friend requests", "Click to configure", 0, 4));
        items.add(new SettingItem(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiEditCrosshair()),Icons.SETTINGS.getResource(),"Custom Crosshair","Custom Crosshair Settings","Click to configure",1,4));


        int x1 = 0;
        int y1 = 0;
        items.sort(Comparator.comparing(SettingItem::getTitle));
        for (SettingItem item : items) {
            item.setxIndex(x1);
            item.setyIndex(y1);
            x1++;
            if (x1 == 3) {
                y1++;
                x1 = 0;
            }
        }
        try {
            callback.put(Autotip.class.getDeclaredField("TIP_MESSAGE_STRING"), o -> Autotip.messageOption = MessageOption.valueOf(o.toString()));
            callback.put(Settings.class.getDeclaredField("MOTION_BLUR_ENABLED"), o -> {
                if (!((boolean) o)) {
                    // If it's been disabled, remove the blur shader.
                    Minecraft.getMinecraft().addScheduledTask(() ->
                            Minecraft.getMinecraft().entityRenderer.stopUseShader());
                }
            });
            callback.put(Settings.class.getDeclaredField("MOTION_BLUR_AMOUNT"), o -> {
                if (Settings.MOTION_BLUR_ENABLED) {
                    // Update motion blur with new intensity.
                    MotionBlurMod.applyShader();
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            for (Object o : HyperiumMainGui.INSTANCE.getSettingsObjects()) {
                for (Field f : o.getClass().getDeclaredFields()) {
                    ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                    SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                    SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                    if (ts == null && ss == null && sliderSetting == null)
                        continue;
                    Consumer<Object> objectConsumer = callback.get(f);
                    if (ts != null) {
                        if (ts.mods()) {
                            getCategory(ts.category()).addToggle(ts.name(), f, objectConsumer, ts.enabled(), o);
                        }

                    } else if (ss != null) {
                        if (!ss.mods())
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
                            try {
                                Double value = Double.valueOf(f.get(o).toString());
                                getCategory(sliderSetting.category()).getComponents().add(new OverlaySlider(sliderSetting.name(), sliderSetting.min(), sliderSetting.max(),
                                        value.floatValue(), aFloat -> {
                                    try {
                                        if (sliderSetting.isInt()) {
                                            f.set(o, aFloat.intValue());
                                        } else
                                            f.set(o, aFloat);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    if (objectConsumer != null)
                                        objectConsumer.accept(aFloat);
                                }, sliderSetting.round(), sliderSetting.enabled()));

                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        levelhead.getComponents().add(new OverlayLabel("Run /levelhead or click here to access more settings", true, () -> {
            if (Minecraft.getMinecraft().thePlayer != null)
                new LevelHeadGui(((Levelhead) Hyperium.INSTANCE.getModIntegration().getLevelhead())).display();
        }));
        autotip.getComponents().add(new OverlayLabel("Run /autotip to access more settings and features", true, () -> {
        }));
        chromahud.getComponents().add(new OverlayLabel("Run /chromahud or click here to access more settings", true, () -> {
            if (Minecraft.getMinecraft().thePlayer != null)
                new GeneralConfigGui(((ChromaHUD) Hyperium.INSTANCE.getModIntegration().getChromaHUD())).display();
        }));
        spotify.getComponents().add(new OverlayButton("Move Player", () -> {
            if (Spotify.instance != null) {
                new SpotifyGui().show();
            } else {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Unable to load spotify!");
            }
        }));

    }

    private HyperiumOverlay getCategory(Category settingsCategory) {
        switch (settingsCategory) {
            case AUTOTIP:
                return autotip;
            case LEVEL_HEAD:
                return levelhead;
            case VANILLA_ENCHANTMENTS:
                return vanilla;
            case MOTION_BLUR:
                return motionblur;
            case CHROMAHUD:
                return chromahud;
            case AUTO_GG:
                return autoGG;
            case ANIMATIONS:
                return animations;
            case SPOTIFY:
                return spotify;
            case AUTOFRIEND:
                return autoFriend;

        }
        throw new IllegalArgumentException(settingsCategory + " Cannot be used in mods!");
    }

    @Override
    public void drawTabIcon() {
        Icons.FA_WRENCH.bind();
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
        return "Mods";
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
    }
}
