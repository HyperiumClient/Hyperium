package cc.hyperium.utils;

import cc.hyperium.installer.InstallerFrame;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class StaffUtils {
    private static final HashMap<UUID, ChatColor> STAFF_CACHE = new HashMap<>();
    public static boolean isStaff(UUID uuid) {
        return STAFF_CACHE.keySet().contains(uuid);
    }

    public static ChatColor getColor(UUID uuid) {
        return STAFF_CACHE.get(uuid);
    }

    public static HashMap<UUID, ChatColor> getStaff() throws IOException {
        HashMap<UUID, ChatColor> staff = new HashMap<>();
        String content = InstallerFrame.get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/files/staff.json");
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            UUID uuid = UUID.fromString(item.getString("uuid"));
            ChatColor color = ChatColor.valueOf(item.getString("color").toUpperCase());
            staff.put(uuid, color);
        }
        return staff;
    }

    public static void clearCache() throws IOException {
        STAFF_CACHE.clear();
        STAFF_CACHE.putAll(getStaff());
    }
}
