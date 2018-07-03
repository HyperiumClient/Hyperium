package cc.hyperium.handlers.handlers.animation.cape;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.installer.utils.DownloadTask;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.CapeUtils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

public class CapeHandler {

    public static final ReentrantLock LOCK = new ReentrantLock();
    private final ConcurrentHashMap<UUID, ICape> capes = new ConcurrentHashMap<>();
    private final ResourceLocation loadingResource = new ResourceLocation("");
    private final File CACHE_DIR;

    public CapeHandler() {

        CACHE_DIR = new File(Hyperium.folder, "CACHE_DIR");
        CACHE_DIR.mkdir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CACHE_DIR.delete();
        }));
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        ICape selfCape = capes.get(id);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        try {
            LOCK.lock();

            for (ICape cape : capes.values()) {
//                if (selfCape != null && selfCape.equals(cape))
//                    continue;
                cape.delete(Minecraft.getMinecraft().getTextureManager());
            }
            capes.clear();
//            if (selfCape != null)
//                capes.put(id, selfCape);
        } finally {
            LOCK.unlock();
        }
    }

    public void loadStaticCape(final UUID uuid, String url) {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);

        ResourceLocation resourceLocation = new ResourceLocation(
                String.format("hyperium/capes/%s.png", System.nanoTime())
        );


        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {

            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {
                return CapeUtils.parseCape(image);
            }

            @Override
            public void skinAvailable() {
                CapeHandler.this.setCape(uuid, new StaticCape(resourceLocation));
            }
        });
        try {
            LOCK.lock();
            textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        } catch (Exception e) {

        } finally {
            LOCK.unlock();
        }
    }

    public void setCape(UUID uuid, ICape cape) {
        capes.put(uuid, cape);
    }

    public void loadDynamicCape(final UUID uuid, String url) throws IOException, ExecutionException, InterruptedException {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);

//
        DownloadTask task = new DownloadTask(
                url,
                CACHE_DIR.getAbsolutePath());
        task.execute();
        task.get();
        List<BufferedImage> images = new ArrayList<>();

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream stream = ImageIO.createImageInputStream(new File(CACHE_DIR, task.getFileName()));
        System.out.println(stream == null);
        reader.setInput(stream);

        int frames = reader.getNumImages(true);
        BufferedImage base = null;
        for (int i = 0; i < frames; i++) {
            BufferedImage read = reader.read(i);
            if (base == null)
                base = read;
            else {
                base.getGraphics().drawImage(read, 0, 0, null);
            }


            BufferedImage bufferedImage = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(base, 0, 0, null);
            int baseWidth = 10;
            int baseHeight = 16;


            double scaleFac = 1;
            for (int j = 0; j < 8; j++) {
                if (baseWidth * Math.pow(2, j) > bufferedImage.getWidth()) {
                    scaleFac = Math.pow(2, j);
                    break;
                }
            }

            int width = (int) (22 * scaleFac);
            int height = (int) (17 * scaleFac);
            BufferedImage formatted = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = formatted.getGraphics();
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, width, height);
            graphics.drawImage(bufferedImage, (int) scaleFac, (int) scaleFac, (int) (scaleFac + baseWidth * scaleFac), (int) (scaleFac + baseHeight * scaleFac), null);
            File outputfile = new File(CACHE_DIR, i + ".png");
            ImageIO.write(formatted, "png", outputfile);
            System.out.println(bufferedImage.getWidth() + " " + bufferedImage.getHeight());
            images.add(CapeUtils.parseCape(formatted));
        }


        Minecraft.getMinecraft().addScheduledTask(() -> {
            ArrayList<ResourceLocation> locations = new ArrayList<>();
            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            int i = 0;
            try {

                for (BufferedImage image : images) {

                    ResourceLocation resourceLocation = new ResourceLocation(
                            String.format("hyperium/dynamic_capes/%s_%s.png", uuid, i));
                    locations.add(resourceLocation);

                    CapeTexture capeTexture = new CapeTexture(image);
                    textureManager.loadTexture(resourceLocation, capeTexture);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setCape(uuid, new DynamicCape(locations, frames, frames));
        });


    }

    public ResourceLocation getCape(final AbstractClientPlayer player) {
        UUID uuid = player.getUniqueID();

        if (isRealPlayer(uuid)) {
            ICape cape = capes.getOrDefault(uuid, null);
            if (cape == null) {
                Multithreading.runAsync(() -> {
                    HyperiumPurchase hyperiumPurchase = PurchaseApi.getInstance()
                            .getPackageSync(uuid);
                    JsonHolder holder = hyperiumPurchase.getPurchaseSettings().optJSONObject("cape");
                    holder.put("type", "CUSTOM");
                    holder.put("url", "https://static.sk1er.club/hyperium_files/lego10.gif");
                    String s = holder
                            .optString("type");
                    if (s.equalsIgnoreCase("CUSTOM")) {
                        String url = holder.optString("url");
                        if (!url.isEmpty()) {
                            try {
                                loadDynamicCape(uuid, url);
                            } catch (IOException | ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    } else if (!s.isEmpty()) {
                        JsonHolder jsonHolder = PurchaseApi.getInstance().getCapeAtlas()
                                .optJSONObject(s);
                        String url = jsonHolder.optString("url");
                        if (!url.isEmpty()) {
                            loadStaticCape(uuid, url);
                            return;
                        }
                    }
                    loadStaticCape(uuid,
                            "http://s.optifine.net/capes/" + player.getGameProfile().getName()
                                    + ".png");
                });
                return capes.getOrDefault(uuid, NullCape.INSTANCE).get();
            }

            if (cape.equals(NullCape.INSTANCE)) {
                return null;
            }
            return cape.get();
        } else {
            return null;
        }
    }

    public boolean isRealPlayer(UUID uuid) {
        String s = uuid.toString().replace("-", "");
        if (s.length() == 32 && s.charAt(12) != '4') {
            return false;
        } else {
            return true;
        }
    }


    public void deleteCape(UUID id) {
        this.capes.remove(id);
    }
}
