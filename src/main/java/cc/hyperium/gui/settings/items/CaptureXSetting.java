package cc.hyperium.gui.settings.items;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.gui.settings.SettingGui;
import cc.hyperium.gui.settings.components.SelectionItem;
import cc.hyperium.mods.capturex.CaptureMode;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class CaptureXSetting extends SettingGui {
    private DefaultConfig config;

    @ConfigOpt
    private static String modeStr = "OFF";

    public static CaptureMode mode;

    @ConfigOpt
    public static int captureLength = 1;

    private SelectionItem<CaptureMode> modeSelection;

    private SelectionItem<Integer> lengthSelection;

    public CaptureXSetting(GuiScreen previous) {
        super("CAPTUREX", previous);
        config = Hyperium.CONFIG;
        config.register(this);
    }

    @Override
    protected void pack() {
        super.pack();
        mode = CaptureMode.valueOf(modeStr);
        settingItems.add(modeSelection = new SelectionItem<>(0, getX(), getDefaultItemY(0),  width - getX() * 2, "CAPTURE MODE", i -> {
            ((SelectionItem)i).nextItem();
            mode = ((SelectionItem<CaptureMode>) i).getSelectedItem();
            modeStr = mode.toString();
        }));
        modeSelection.addItems(Arrays.asList(CaptureMode.values()));
        modeSelection.setSelectedItem(mode);

        settingItems.add(lengthSelection = new SelectionItem<>(1, getX(), getDefaultItemY(1), width - getX() * 2, "CAPTURE LENGTH", i -> {
            ((SelectionItem)i).nextItem();
            captureLength = ((SelectionItem<Integer>)i).getSelectedItem();
        }));
        lengthSelection.addItems(Arrays.asList(1, 3, 5, 8, 10));
        lengthSelection.setSelectedItem(captureLength);
    }

    private int getDefaultItemY(int i) {
        return getY()+25 + i * 15;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        config.save();
    }
}
