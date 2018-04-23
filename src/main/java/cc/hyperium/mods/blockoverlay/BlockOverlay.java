package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

import java.io.*;

public class BlockOverlay extends AbstractMod {
    protected static boolean alwaysRender;
    protected static boolean isChroma;
    protected static boolean openGui;
    protected static float lineWidth;
    protected static float red;
    protected static float green;
    protected static float blue;
    protected static float alpha;
    protected static int chromaSpeed;
    protected static BlockOverlayMode mode;
    private final Metadata meta;

    public BlockOverlay() {
        meta = new Metadata(this, "BlockOverlay", "1.0", "aycy & powns");
        meta.setDisplayName(ChatColor.RED + "BlockOverlay");
    }

    @Override
    public AbstractMod init() {
        BlockOverlay.alwaysRender = false;
        BlockOverlay.isChroma = false;
        BlockOverlay.openGui = false;
        BlockOverlay.lineWidth = 2.0f;
        BlockOverlay.red = 1.0f;
        BlockOverlay.green = 1.0f;
        BlockOverlay.blue = 1.0f;
        BlockOverlay.alpha = 1.0f;
        BlockOverlay.chromaSpeed = 1;
        BlockOverlay.mode = BlockOverlayMode.DEFAULT;
        EventBus.INSTANCE.register(this);
        EventBus.INSTANCE.register(new BlockOverlayRender());
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new BlockOverlayCommand());
        this.loadConfig();
        return this;
    }

    public void loadConfig() {
        try {
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/config/blockOverlay.cfg");
            if (file.exists()) {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final String name = reader.readLine();
                for (final BlockOverlayMode mode : BlockOverlayMode.values()) {
                    if (mode.name.equals(name)) {
                        BlockOverlay.mode = mode;
                        break;
                    }
                }
                BlockOverlay.red = Float.parseFloat(reader.readLine());
                BlockOverlay.green = Float.parseFloat(reader.readLine());
                BlockOverlay.blue = Float.parseFloat(reader.readLine());
                BlockOverlay.alpha = Float.parseFloat(reader.readLine());
                BlockOverlay.alwaysRender = reader.readLine().equals("true");
                BlockOverlay.isChroma = reader.readLine().equals("true");
                BlockOverlay.chromaSpeed = Integer.parseInt(reader.readLine());
                BlockOverlay.lineWidth = Float.parseFloat(reader.readLine());
                reader.close();
            }
        } catch (Exception exception) {
            System.out.println("Error occurred while loading BlockOverlay configuration");
            exception.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/config/blockOverlay.cfg");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(BlockOverlay.mode.name + "\r\n" + BlockOverlay.red + "\r\n" + BlockOverlay.green + "\r\n" + BlockOverlay.blue + "\r\n" + BlockOverlay.alpha + "\r\n" + BlockOverlay.alwaysRender + "\r\n" + BlockOverlay.isChroma + "\r\n" + BlockOverlay.chromaSpeed + "\r\n" + BlockOverlay.lineWidth);
            writer.close();
        } catch (Exception exception) {
            System.out.println("Error occurred while saving BlockOverlay configuration");
            exception.printStackTrace();
        }
    }

    @InvokeEvent
    public void onClientTick(final TickEvent event) {
        if (BlockOverlay.openGui) {
            BlockOverlay.openGui = false;
            Minecraft.getMinecraft().displayGuiScreen(new BlockOverlayGui());
        }
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }
}
