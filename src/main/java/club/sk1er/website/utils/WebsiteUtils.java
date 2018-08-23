package club.sk1er.website.utils;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;

public class WebsiteUtils {

    public static String comma(Number number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    public static String buildRatio(int a, int b) {

        double c = (double) a;
        double d = (double) b;
        if (a + b == 0) {
            return "0";
        }
        if (b == 0) {
            return Character.toString('\u221e');
        }
        if (a == 0) {
            return "0";
        }
        double e = c / d;
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(e);
    }

    public static String getColor(String in) {
        String color = "";
        switch (in.toLowerCase()) {
            case "green":
                color = "§a";
                break;
            case "gold":
                color = "§6";
                break;
            case "light_purple":
                color = "§d";
                break;
            case "yellow":
                color = "§e";
                break;
            case "white":
                color = "§f";
                break;
            case "blue":
                color = "§9";
                break;
            case "dark_green":
                color = "§2";
                break;
            case "dark_red":
                color = "§4";
                break;
            case "dark_aqua":
                color = "§3";
                break;
            case "dark_purple":
                color = "§5";
                break;
            case "dark_gray":
                color = "§7";
                break;
            case "black":
                color = "§0";
                break;
        }
        return color;
    }

    public static long get(JsonObject tmp, String path) {
        try {
            if (path.contains("#")) {
                long max = path.split("#").length;
                long cur = 0;
                JsonObject curent = tmp;
                for (String s : path.split("#")) {
                    if (cur >= max - 1) {
                        return (curent.has(s) ? curent.get(s).getAsLong() : 0);
                    } else {
                        curent = curent.has(s) ? curent.get(s).getAsJsonObject() : new JsonObject();
                    }
                    cur++;
                }
            } else {
                return tmp.has(path) ? tmp.get(path).getAsLong() : 0;

            }

            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
