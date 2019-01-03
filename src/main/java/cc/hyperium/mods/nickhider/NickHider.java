package cc.hyperium.mods.nickhider;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.internal.addons.annotations.Instance;
import cc.hyperium.mixins.gui.MixinGuiScreenBook;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NickHider {
    public static final String MOD_ID = "nick_hider";
    public static final String MOD_NAME = "Sk1er nick Hider";
    public static final String VERSION = "3.0";
    @Instance
    public static NickHider INSTANCE;
    private final Pattern newNick = Pattern.compile("We've generated a random username for you: \\s*(?<nick>\\S+)");
    private final List<Nick> nicks = new ArrayList<>();
    private File suggestedConfigurationFile = new File(Hyperium.folder, "nick_data.json");
    private HashMap<String, String> cache = new HashMap<>();
    private HashMap<String, String> remaps = new HashMap<>();
    private Set<String> usedNicks = new HashSet<>();
    private Sk1erMod sk1erMod;
    private NickHiderConfig config;
    private boolean forceDown = false;
    private boolean extendedUse = false;
    private String override = null;

    public NickHider() {
        INSTANCE = this;
    }

    public Set<String> getUsedNicks() {
        return usedNicks;
    }

    public String getPseudo_key() {
        return config.getPseudo_key();
    }

    public void setPseudo_key(String pseudo_key) {
        config.setPseudo_key(pseudo_key);
    }

    private List<String> namesDatabase = new ArrayList<>();

    public String getPseudo(String input) {
        int i = input.hashCode() + getPseudo_key().hashCode();
        if (i < 0)
            i = -i;

        int size = namesDatabase.size();
        if (size == 0)
            return "Player-error";

        return "Player-" + namesDatabase.get(i % size);
    }

    public void init() {
        sk1erMod = new Sk1erMod(MOD_ID, VERSION, object -> {
            if (!object.optBoolean("enabled"))
                forceDown = true;
            if (object.optBoolean("extended"))
                extendedUse = true;
        });
        sk1erMod.checkStatus();
        Multithreading.runAsync(() -> {
            String s = sk1erMod.rawWithAgent("https://sk1er.club/words.txt?uuid=" + Minecraft.getMinecraft().getSession().getProfile().getId());
            namesDatabase.addAll(Arrays.asList(s.split("\n")));
        });
        if (suggestedConfigurationFile.exists()) {
            try {
                FileReader baseReader = new FileReader(this.suggestedConfigurationFile);
                BufferedReader bufferedReader = new BufferedReader(baseReader);

                StringBuilder lines = new StringBuilder();

                for (String line : bufferedReader.lines().collect(Collectors.toList())) {
                    lines.append(line);
                }

                baseReader.close();
                bufferedReader.close();

                boolean broken = false;

                try {
                    new JsonParser().parse(lines.toString().trim());
                } catch (JsonParseException e) {
                    broken = true;
                }

                if (!broken) {
                    this.config = new Gson().fromJson(lines.toString().trim(), NickHiderConfig.class);
                }
            } catch (IOException e) {
                this.config = null;

                FileUtils.deleteQuietly(this.suggestedConfigurationFile);
            }
        }
        if (config == null) {
            this.config = new NickHiderConfig();
        }

        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandNickHider());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String s = new Gson().toJson(this.config);
            try {
                FileUtils.write(suggestedConfigurationFile, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public boolean isExtendedUse() {
        return extendedUse;
    }

    @InvokeEvent
    public void bookCheck(RenderEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen == null)
            return;

        if (currentScreen instanceof GuiScreenBook) {
            NBTTagList bookPages = ((MixinGuiScreenBook) currentScreen).getBookPages();
            int currPage = ((MixinGuiScreenBook) currentScreen).getCurrPage();
            if (currPage < bookPages.tagCount()) {
                try {
                    String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(IChatComponent.Serializer.jsonToComponent(bookPages.getStringTagAt(currPage)).getUnformattedText().replace("\n", " "));
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

    public void setOwnName(String name) {
        override = name;
        String name1 = Minecraft.getMinecraft().getSession().getProfile().getName();
        usedNicks.remove(name1.toLowerCase());
        nicks.removeIf(nick -> nick.oldName.equalsIgnoreCase(name1));
        remap(name1, name);
    }

    public String out(String chat) {
        if (isEnabled()) {
            for (Nick nick : nicks) {
                if (!nick.oldName.equalsIgnoreCase(Minecraft.getMinecraft().getSession().getUsername()))
                    chat = Pattern.compile(nick.newName, Pattern.CASE_INSENSITIVE).matcher(chat).replaceAll(nick.oldName);
            }
        }
        return chat;
    }

    public String[] tabComplete(String[] in, String soFar) {
        String[] split = soFar.split(" ");
        String tmp = split[split.length - 1];
        List<String> tmp1 = new ArrayList<>();
        for (Nick nick : nicks) {
            if (nick.newName.toLowerCase().startsWith(tmp.toLowerCase()))
                tmp1.add(nick.newName);

        }
        String[] re = new String[tmp1.size()];
        for (int i = 0; i < tmp1.size(); i++) {
            re[i] = tmp1.get(i);
        }
        return ObjectArrays.concat(in, re, String.class);
    }

    public List<Nick> getNicks() {
        return nicks;
    }

    public HashMap<String, String> getCache() {
        return cache;
    }

    @InvokeEvent
    public void profileCheck(TickEvent event) {
        if (!isEnabled())
            return;

        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer == null)
            return;
        NetHandlerPlayClient sendQueue = thePlayer.sendQueue;
        if (sendQueue == null)
            return;

        UUID id = Minecraft.getMinecraft().getSession().getProfile().getId();
        String name = Minecraft.getMinecraft().getSession().getProfile().getName();
        for (NetworkPlayerInfo networkPlayerInfo : sendQueue.getPlayerInfoMap()) {
            GameProfile gameProfile = networkPlayerInfo.getGameProfile();
            if (gameProfile.getId() != null && gameProfile.getId().equals(id)) {
                if (!gameProfile.getName().equalsIgnoreCase(name)) {
                    remap(gameProfile.getName(), override == null ? name : override);
                }
            } else if (!config.isSelfOnly()) {
                remap(gameProfile.getName(), getPseudo(gameProfile.getName()));
            }
        }


    }

    public boolean isHideSkins() {
        return config.isHideSkins();
    }

    public void setHideSkins(boolean hideSkins) {
        this.config.setHideSkins(hideSkins);
    }

    public void reset() {
        nicks.clear();
        cache.clear();
        usedNicks.clear();
    }

    public void remap(String key, String newKey) {
        key = key.toLowerCase();
        if (usedNicks.contains(key))
            return;
        if (key.isEmpty() || key.contains(" "))
            return;
        usedNicks.add(key);
        remaps.put(key, newKey);
        Nick nick = new Nick(Pattern.compile(key.toLowerCase(), Pattern.CASE_INSENSITIVE), key, newKey);
        nicks.add(nick);
        cache.clear();
    }

    public String apply(String input) {
        if (config == null) {
            config = new NickHiderConfig();
        }
        if (forceDown)
            return input;
        if (!config.isEnabled())
            return input;
        if (nicks.size() == 0)
            return input;
        if (cache.size() > 5000)
            cache.clear();
        return cache.computeIfAbsent(input, s -> {
            String base = input;
            for (Nick nick : nicks) {
                base = nick.pattern.matcher(base).replaceAll(nick.newName);
            }
            return base;
        });
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public boolean isSelfOnly() {
        return config.isSelfOnly();
    }

    public void toggle() {
        config.setEnabled(!config.isEnabled());
        reset();
    }

    public void toggleSelf() {
        config.setSelfOnly(!config.isSelfOnly());
        reset();
    }

    class Nick {

        public Pattern pattern;
        public String oldName;
        public String newName;

        public Nick(Pattern pattern, String oldName, String newName) {
            this.pattern = pattern;
            this.oldName = oldName;
            this.newName = newName;
        }
    }
}
