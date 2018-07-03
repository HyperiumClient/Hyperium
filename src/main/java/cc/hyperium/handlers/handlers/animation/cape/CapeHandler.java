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
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrackMeta;
import org.jcodec.common.JCodecUtil;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
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

    public void loadDynamicCape(final UUID uuid, String url) throws IOException, ExecutionException, InterruptedException, JCodecException {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);
        DownloadTask task = new DownloadTask(
                url,
                CACHE_DIR.getAbsolutePath());
        task.execute();
        task.get();
        File downloaded = new File(CACHE_DIR, task.getFileName());
        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(downloaded));
        DemuxerTrackMeta dtm = JCodecUtil.createDemuxer(JCodecUtil.detectFormat(downloaded), downloaded).getVideoTracks().get(0).getMeta();
        int totalFrames = dtm.getTotalFrames();
        double totalDuration = dtm.getTotalDuration();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        List<BufferedImage> images = new ArrayList<>();

        for (int i = 0; i < totalFrames; i++) {
            grab.seekToFramePrecise(i);
            Picture nativeFrame = grab.getNativeFrame();
            BufferedImage convert = AWTUtil.toBufferedImage(nativeFrame);
            images.add(convert);
            File outputfile = new File(CACHE_DIR, i + ".png");
            ImageIO.write(convert, "png", outputfile);
        }
        Minecraft.getMinecraft().addScheduledTask(() -> {
            ArrayList<ResourceLocation> locations = new ArrayList<>();
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
            setCape(uuid, new DynamicCape(locations, totalDuration, totalFrames));
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
                    holder.put("url", "https://static.sk1er.club/hyperium_files/lego6.mp4");
                    String s = holder
                            .optString("type");
                    if (s.equalsIgnoreCase("CUSTOM")) {
                        String url = holder.optString("url");
                        if (!url.isEmpty()) {
                            try {
                                loadDynamicCape(uuid, url);
                            } catch (IOException | ExecutionException | InterruptedException | JCodecException e) {
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
