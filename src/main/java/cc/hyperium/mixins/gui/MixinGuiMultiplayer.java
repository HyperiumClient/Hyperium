package cc.hyperium.mixins.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.mixinsimp.renderer.gui.IMixinGuiMultiplayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen implements IMixinGuiMultiplayer {
    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    private boolean directConnect;
    @Shadow
    private ServerData selectedServer;
    @Shadow
    private GuiScreen parentScreen;
    @Shadow
    private ServerSelectionList serverListSelector;
    @Shadow
    private boolean initialized;
    @Shadow
    private ServerList savedServerList;
    @Shadow
    private LanServerDetector.LanServerList lanServerList;
    @Shadow
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    @Shadow
    private GuiButton btnSelectServer;
    @Shadow
    private GuiButton btnEditServer;
    @Shadow
    private GuiButton btnDeleteServer;
    @Shadow
    private boolean deletingServer;
    @Shadow
    private boolean addingServer;
    @Shadow
    private boolean editingServer;

    @Shadow
    protected abstract void connectToServer(ServerData server);

    @Shadow
    public abstract void createButtons();

    @Shadow
    protected abstract void refreshServerList();

    @Shadow
    public abstract void connectToSelected();

    @Overwrite
    public void selectServer(int index) {
        this.serverListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;

        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;

            if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
        }
        if (index == 0) {
            this.btnDeleteServer.enabled = false;
            this.btnEditServer.enabled = false;
        }
    }


    @Override
    public void makeDirectConnect() {
        directConnect = true;
    }

    @Override
    public void setIp(ServerData ip) {
        this.selectedServer = ip;
    }


    /**
     * @author Sk1er
     */
    @Overwrite
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        int i = this.serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);
        String string = Hyperium.INSTANCE.isDevEnv() ? "getSize" : "func_148127_b";
        if (keyCode == 63) {
            this.refreshServerList();
        } else {
            if (i >= 0) {
                if (keyCode == 200) {
                    if (isShiftKeyDown()) {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                            if (i - 1 == 0)
                                return;
                            this.savedServerList.swapServers(i, i - 1);
                            this.selectServer(this.serverListSelector.func_148193_k() - 1);
                            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            this.serverListSelector.func_148195_a(this.savedServerList);
                        }
                    } else if (i > 0) {
                        this.selectServer(this.serverListSelector.func_148193_k() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                            if (this.serverListSelector.func_148193_k() > 0) {
                                Method method = null;

                                try {
                                    method = ServerSelectionList.class.getDeclaredMethod(string);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();

                                }
                                Object invoke = null;
                                try {
                                    invoke = method.invoke(this.serverListSelector);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                this.selectServer(((int) invoke) - 1);
                                this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                            } else {
                                this.selectServer(-1);
                            }
                        }
                    } else {
                        this.selectServer(-1);
                    }
                } else if (keyCode == 208) {
                    Method method = null;
                    try {
                        method = ServerSelectionList.class.getDeclaredMethod(string);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    Object invoke = null;
                    try {
                        invoke = method.invoke(this.serverListSelector);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    int size = (int) invoke;
                    if (isShiftKeyDown()) {
                        if (i < this.savedServerList.countServers() - 1) {
                            if (i - 1 == 0 || i == 0)
                                return;
                            this.savedServerList.swapServers(i, i + 1);
                            this.selectServer(i + 1);
                            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            this.serverListSelector.func_148195_a(this.savedServerList);
                        }
                    } else if (i < size) {
                        this.selectServer(this.serverListSelector.func_148193_k() + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());

                        if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                            if (this.serverListSelector.func_148193_k() < size - 1) {
                                this.selectServer(size + 1);
                                this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                            } else {
                                this.selectServer(-1);
                            }
                        }
                    } else {
                        this.selectServer(-1);
                    }
                } else if (keyCode != 28 && keyCode != 156) {
                    super.keyTyped(typedChar, keyCode);
                } else {
                    this.actionPerformed((GuiButton) this.buttonList.get(2));
                }
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (!this.initialized) {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            } catch (Exception exception) {
                logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }
            //Ignore cast warning
            this.serverListSelector = new ServerSelectionList((GuiMultiplayer) (Object) this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.func_148195_a(this.savedServerList);
        } else {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }

        this.createButtons();
    }
}
