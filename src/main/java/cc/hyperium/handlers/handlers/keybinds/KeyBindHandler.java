/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.gui.ModConfigGui;
import cc.hyperium.gui.NameHistoryGui;
import cc.hyperium.gui.integrations.HypixelFriendsGui;
import cc.hyperium.gui.integrations.QueueModGui;
import cc.hyperium.gui.settings.items.CosmeticSettings;
import cc.hyperium.handlers.handlers.DabHandler;
import cc.hyperium.handlers.handlers.FlossDanceHandler;
import cc.hyperium.integrations.spotify.Spotify;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import utils.JsonHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class KeyBindHandler {

    public static final HyperiumBind nameHistory = new HyperiumBind("nameHistory", Keyboard.KEY_H) {
        @Override
        public void onPress() {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new NameHistoryGui());
        }
    };
    private static final Map<Integer, Integer> mouseBinds = new HashMap<>();
    private final KeyBindConfig keyBindConfig;
    // Case insensitive treemap
    private final TreeMap<String, HyperiumBind> keybinds = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public final HyperiumBind friends = new HyperiumBind("friends", Keyboard.KEY_L) {
        @Override
        public void onPress() {
            Minecraft.getMinecraft().displayGuiScreen(new HypixelFriendsGui());
        }
    };
    public final HyperiumBind queue = new HyperiumBind("Queue", Keyboard.KEY_K) {
        @Override
        public void onPress() {
            Minecraft.getMinecraft().displayGuiScreen(new QueueModGui());
        }
    };

    public final HyperiumBind debug = new HyperiumBind("DEBUG", Keyboard.KEY_J) {
        @Override
        public void onPress() {
            Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer = !Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer;

        }

        @Override
        public void onRelease() {
            Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer = !Hyperium.INSTANCE.getHandlers().getConfigOptions().alternateFontRenderer;
        }
    };

    public final HyperiumBind invert = new HyperiumBind("Invert (Requires Purchase)", Keyboard.KEY_I) {
        private boolean inverted;

        @Override
        public void onPress() {
            if (!Hyperium.INSTANCE.getCosmetics().getFlipCosmetic().isSelfUnlocked())
                return;
            inverted = !inverted;
            int state = inverted ? CosmeticSettings.flip_type : 0;
            Hyperium.INSTANCE.getHandlers().getFlipHandler().state(Minecraft.getMinecraft().getSession().getProfile().getId(), state);
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", state)));
            Hyperium.INSTANCE.getHandlers().getFlipHandler().resetTick();
        }

        @Override
        public void onRelease() {
            if (!Hyperium.INSTANCE.getCosmetics().getFlipCosmetic().isSelfUnlocked())
                return;
            inverted = !inverted;
            int state = inverted ? CosmeticSettings.flip_type : 0;
            Hyperium.INSTANCE.getHandlers().getFlipHandler().state(Minecraft.getMinecraft().getSession().getProfile().getId(), state);
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "flip_update").put("flip_state", state)));
        }
    };

    public final HyperiumBind flossDance = new HyperiumBind("Floss dance", Keyboard.KEY_P) {
        @Override
        public void onPress() {
            FlossDanceHandler flossDanceHandler = Hyperium.INSTANCE.getHandlers().getFlossDanceHandler();
            UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
            FlossDanceHandler.DanceState currentState = flossDanceHandler.get(uuid);

            if (CosmeticSettings.flossDanceToggle && currentState.isDancing() && !this.wasPressed()) {
                flossDanceHandler.get(uuid).setToggled(false);
                flossDanceHandler.stopDancing(uuid);
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", false)));
                return;
            }

            if (!this.wasPressed()) {
                flossDanceHandler.get(uuid).setToggled(CosmeticSettings.flossDanceToggle);
                flossDanceHandler.startDancing(uuid);
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", true)));

            }
        }


        @Override
        public void onRelease() {
            if (CosmeticSettings.flossDanceToggle) return;
            Hyperium.INSTANCE.getHandlers().getFlossDanceHandler().stopDancing(Minecraft.getMinecraft().getSession().getProfile().getId());
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "floss_update").put("flossing", false)));

        }
    };

    public final HyperiumBind dab = new HyperiumBind("Dab", Keyboard.KEY_B) {
        @Override
        public void onPress() {
            DabHandler dabHandler = Hyperium.INSTANCE.getHandlers().getDabHandler();
            UUID uuid = (Minecraft.getMinecraft().getSession()).getProfile().getId();
            DabHandler.DabState currentState = dabHandler.get(uuid);

            if (CosmeticSettings.dabToggle && currentState.isDabbing() && !this.wasPressed()) {
                dabHandler.get(uuid).setToggled(false);
                dabHandler.stopDabbing(uuid);
                NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));
                return;
            }

            if (!this.wasPressed()) {
                dabHandler.get(uuid).setToggled(CosmeticSettings.dabToggle);
                dabHandler.startDabbing(uuid);
            }
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", true)));
        }


        @Override
        public void onRelease() {
            if (CosmeticSettings.dabToggle) return;

            Hyperium.INSTANCE.getHandlers().getDabHandler().stopDabbing(Minecraft.getMinecraft().getSession().getProfile().getId());
            NettyClient.getClient().write(ServerCrossDataPacket.build(new JsonHolder().put("type", "dab_update").put("dabbing", false)));
        }
    };

    /**
     * Opens GUI on Z key pressed oof - ConorTheOreo
     */
    public final HyperiumBind guikey = new HyperiumBind("Hyperium GUI", Keyboard.KEY_GRAVE) {
        @Override
        public void onPress() {
            new ModConfigGui().show();
        }
    };

    //Hyperium.INSTANCE.getHandlers().getConfigOptions().hideNameTags
    public KeyBindHandler() {
        this.keyBindConfig = new KeyBindConfig(this, Hyperium.folder);

        this.keyBindConfig.load();

        registerKeyBinding(friends);
        registerKeyBinding(nameHistory);
        registerKeyBinding(debug);
        registerKeyBinding(guikey);
        registerKeyBinding(queue);
        registerKeyBinding(dab);
        registerKeyBinding(invert);
        registerKeyBinding(flossDance);
        // Spotify Binds
        HyperiumBind pauseSpotify = new HyperiumBind("Pause Spotify", Keyboard.KEY_COMMA) {
            @Override
            public void onPress() {
                Spotify.instance.pause(true);
            }
        };
        registerKeyBinding(pauseSpotify);
        HyperiumBind resumeSpotify = new HyperiumBind("Resume Spotify", Keyboard.KEY_PERIOD) {
            @Override
            public void onPress() {
                Spotify.instance.pause(false);
            }
        };
        registerKeyBinding(resumeSpotify);

        // Populate mouse bind list in accordance with Minecraft's values.
        for (int i = 0; i < 16; i++) {
            mouseBinds.put(i, -100 + i);
        }
    }

    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onPress();
                    bind.setWasPressed(true);
                }

                if (bind.wasPressed() && !bind.isKeyDown()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                }
            }
        }
    }

    @InvokeEvent
    public void onMouseButton(MouseButtonEvent event) {
        // Dismisses mouse movement input.
        if (event.getValue() >= 0) {
            if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
                for (HyperiumBind bind : this.keybinds.values()) {
                    // Gets Minecraft value of the mouse value and checks to see if it matches a keybind.
                    if (mouseBinds.get(event.getValue()) == bind.getKeyCode()) {
                        bind.onPress();
                        bind.setWasPressed(true);
                    }

                    if (bind.wasPressed() && !bind.isKeyDown()) {
                        bind.onRelease();
                        bind.setWasPressed(false);
                    }
                }
            }
        }
    }

    @InvokeEvent
    public void onGameShutdown(GameShutDownEvent event) {
        this.keyBindConfig.save();
    }

    /**
     * Grabs a binding from the registered keybindings list, this is case-insensitive and
     * any key/name may be provided without fear of causing issues
     *
     * @param name the name or id of the keybinding
     * @return a keybinding instance or null if nothing was found
     */
    public HyperiumBind getBinding(String name) {
        return this.keybinds.getOrDefault(name, null);
    }

    /**
     * Registers a Hyperium KeyBinding here & in the game code so it shows up in the
     * controls menu, allowing the user to modify the keyblind
     *
     * @param bind the hyperium key we wish to register
     */
    public void registerKeyBinding(HyperiumBind bind) {
        this.keybinds.put(bind.getRealDescription(), bind);

        this.keyBindConfig.attemptKeyBindLoad(bind);

        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils
                .add(Minecraft.getMinecraft().gameSettings.keyBindings, bind);
    }

    /**
     * Getter for the amazing KeyBind config
     *
     * @return the keybind config
     */
    public KeyBindConfig getKeyBindConfig() {
        return this.keyBindConfig;
    }

    /**
     * Getter for the all the registered key bindings, this is package
     * private to allow for saving and loading
     *
     * @return the keybinds
     */
    protected TreeMap<String, HyperiumBind> getKeybinds() {
        return this.keybinds;
    }
}
