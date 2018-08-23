package club.sk1er.website.utils;

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
}
