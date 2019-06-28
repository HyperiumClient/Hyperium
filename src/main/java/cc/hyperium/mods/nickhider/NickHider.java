package cc.hyperium.mods.nickhider;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mixins.gui.MixinGuiScreenBook;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.nickhider.command.CommandNickHider;
import cc.hyperium.mods.nickhider.config.NickHiderConfig;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NickHider extends AbstractMod {

    public static NickHider instance;
    private final Pattern newNick = Pattern.compile("We've generated a random username for you: \\s*(?<nick>\\S+)");
    private final List<Nick> nicks = new ArrayList<>();
    private HashMap<String, String> cache = new HashMap<>();
    private Set<String> usedNicks = new HashSet<>();
    private List<String> namesDatabase = new ArrayList<>();
    private File configFile;
    private NickHiderConfig nickHiderConfig;
    private boolean forceDown = false;
    private boolean extendedUse = false;
    private String override = null;
    private ResourceLocation playerSkin;
    private boolean startedLoadingSkin = false;
    private String playerRealSkinType = "default";
    private ResourceLocation playerCape;

    public static final String VERSION = "5.2";

    @Override
    public AbstractMod init() {
        instance = this;

        Sk1erMod sk1erMod = new Sk1erMod("nick_hider", VERSION, object -> {
            if (!object.optBoolean("enabled")) {
                forceDown = true;
            }

            if (object.optBoolean("extended")) {
                extendedUse = true;
            }
        });

        Multithreading.runAsync(() -> {
            String s = sk1erMod.rawWithAgent("https://sk1er.club/words.txt?uuid=" + Minecraft.getMinecraft().getSession().getProfile().getId());
            namesDatabase.addAll(Arrays.asList(s.split("\n")));
        });

        sk1erMod.checkStatus();
        configFile = new File(Hyperium.folder, "nickhider.json");

        if (configFile.exists()) {
            try {
                String s = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                nickHiderConfig = new Gson().fromJson(s, NickHiderConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (nickHiderConfig == null) {
            nickHiderConfig = new NickHiderConfig();
        }

        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(new CommandNickHider());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String s = new Gson().toJson(nickHiderConfig);
            try {
                FileUtils.write(configFile, s, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        return this;
    }

    @InvokeEvent
    public void bookCheck(TickEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen == null) return;

        if (currentScreen instanceof GuiScreenBook) {
            NBTTagList bookPages = ((MixinGuiScreenBook) currentScreen).getBookPages();
            int currPage = ((MixinGuiScreenBook) currentScreen).getCurrPage();

            if (currPage < bookPages.tagCount()) {
                try {
                    String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(
                        IChatComponent.Serializer.jsonToComponent(bookPages.getStringTagAt(currPage)).getUnformattedText().replace("\n", " "));
                    Matcher matcher = newNick.matcher(textWithoutFormattingCodes);
                    if (matcher.find()) {
                        String nick = matcher.group("nick");
                        remap(nick, override == null ? Minecraft.getMinecraft().getSession().getProfile().getName() : override);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @InvokeEvent
    public void profileCheck(TickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        NetHandlerPlayClient sendQueue = player.sendQueue;
        if (sendQueue == null) return;

        for (NetworkPlayerInfo playerInfo : sendQueue.getPlayerInfoMap()) {
            GameProfile gameProfile = playerInfo.getGameProfile();
            if (gameProfile.getId() != null && gameProfile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                if (!gameProfile.getName().equalsIgnoreCase(Minecraft.getMinecraft().getSession().getProfile().getName())) {
                    remap(gameProfile.getName(), override == null ? Minecraft.getMinecraft().getSession().getProfile().getName() : override);
                }
            } else if (nickHiderConfig.isHideOtherNames()) {
                remap(gameProfile.getName(), getPseudo(gameProfile.getName()));
            }
        }
    }

    public void setOwnName(String name) {
        override = name;
        String oldName = Minecraft.getMinecraft().getSession().getProfile().getName();
        usedNicks.remove(oldName.toLowerCase());
        nicks.removeIf(nick -> nick.oldName.equalsIgnoreCase(oldName));
        remap(oldName, name);
    }

    public void remap(String key, String newKey) {
        key = key.toLowerCase();
        if (usedNicks.contains(key)) {
            return;
        }

        if (key.isEmpty() || key.contains(" ")) {
            return;
        }

        usedNicks.add(key);
        Nick nick = new Nick(Pattern.compile(key.toLowerCase(), Pattern.CASE_INSENSITIVE), key, key.length() > 2 ? newKey : key);
        nicks.add(nick);

        Comparator<Nick> c = Comparator.comparingInt(o -> o.oldName.length());
        nicks.sort(c.reversed());

        cache.clear();
    }

    public String apply(String input) {
        if (nickHiderConfig == null) {
            nickHiderConfig = new NickHiderConfig();
        }

        if (!nickHiderConfig.isMasterEnabled()) {
            return input;
        }

        if (forceDown) {
            return input;
        }

        if (!nickHiderConfig.isHideNames()) {
            return input;
        }

        if (cache.size() > 5000) {
            cache.clear();
        }

        return cache.computeIfAbsent(input, s -> {
            String base = input;
            for (Nick nick : nicks) {
                base = nick.pattern.matcher(base).replaceAll(nick.newName);
            }

            return base;
        });
    }

    public void toggleHideNames() {
        nickHiderConfig.setHideNames(!nickHiderConfig.isHideNames());
        reset();
    }

    public void toggleSelf() {
        nickHiderConfig.setHideOtherNames(!nickHiderConfig.isHideOtherNames());
        reset();
    }

    public String out(String chat) {
        if (!nickHiderConfig.isHideNames()) {
            return chat;
        }

        for (Nick nick : nicks) {
            chat = Pattern.compile(nick.newName, Pattern.CASE_INSENSITIVE).matcher(chat).replaceAll(nick.oldName);
        }

        return chat;
    }

    public String[] tabComplete(String[] in, String soFar) {
        if (!nickHiderConfig.isHideNames()) {
            return in;
        }

        String[] split = soFar.split(" ");
        String tmp = split[split.length - 1];
        List<String> tmp1 = new ArrayList<>();
        for (int i = 0; i < in.length; i++) {
            in[i] = apply(in[i]);
        }

        for (Nick nick : nicks) {
            if (nick.newName.toLowerCase().startsWith(tmp.toLowerCase())) {
                tmp1.add(nick.newName);
            }
        }

        HashSet<String> strings = Sets.newHashSet(in);
        strings.addAll(tmp1);
        return strings.toArray(new String[0]);
    }

    public void reset() {
        nicks.clear();
        cache.clear();
        usedNicks.clear();
    }

    public ResourceLocation getPlayerSkin() {
        if (playerSkin == null && !startedLoadingSkin) {
            startedLoadingSkin = true;
            Minecraft.getMinecraft().getSkinManager().loadProfileTextures(Minecraft.getMinecraft().getSession().getProfile(), (profile, location, profileTexture) -> {
                if (profile == MinecraftProfileTexture.Type.SKIN) {
                    playerSkin = location;
                    playerRealSkinType = profileTexture.getMetadata("model");
                    if (playerRealSkinType == null) {
                        playerRealSkinType = "default";
                    }
                } else if (profile == MinecraftProfileTexture.Type.CAPE) {
                    playerCape = location;
                }
            }, true);
        }

        return playerSkin;
    }

    public String getPseudo(String input) {
        int i = input.hashCode() + nickHiderConfig.getPseudoKey().hashCode();
        if (i < 0) {
            i = -i;
        }

        int size = namesDatabase.size();
        if (size == 0) {
            return "Player-error";
        }

        return nickHiderConfig.getPrefix() + namesDatabase.get(i % size) + nickHiderConfig.getSuffix() + (nickHiderConfig.getPrefix().
            equalsIgnoreCase("Player-") ? "" : getStar());
    }

    private String getStar() {
        String s = "*";
        int i = s.hashCode();
        if (s.isEmpty() || i != 42) {
            throw new IllegalStateException("Potential illegal nickhider modification found. If you did NOT edit NickHider, contact Sk1er (" + s + ")");
        }

        return s;
    }

    public List<Nick> getNicks() {
        return nicks;
    }

    public Set<String> getUsedNicks() {
        return usedNicks;
    }

    public boolean isExtendedUse() {
        return extendedUse;
    }

    public ResourceLocation getPlayerCape() {
        return playerCape;
    }

    public String getPlayerRealSkinType() {
        return playerRealSkinType;
    }

    public NickHiderConfig getNickHiderConfig() {
        return nickHiderConfig;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Nick Hider", VERSION, "Sk1er");
    }

    public class Nick {
        public Pattern pattern;
        public String oldName;
        String newName;

        public Nick(Pattern pattern, String oldName, String newName) {
            this.pattern = pattern;
            this.oldName = oldName;
            this.newName = newName;
        }
    }
}
