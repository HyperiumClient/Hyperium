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

    public static void main(String[] args) {
        String in = " handler.write(strongBr(\"Bedwars level: \", getUtils().getBedwarsLevel(bedwars.optInt(\"Experience\") + bedwars.optInt(\"Experience_new\"))));\n" +
                "        handler.write(strongBr(\"Beds Broken: \", bedwars.optInt(\"beds_broken_bedwars\")));\n" +
                "        handler.writeLine(1);\n" +
                "        handler.write(strongBr(\"Final Kills: \", bedwars.optInt(\"final_kills_bedwars\")));\n" +
                "        handler.write(strongBr(\"Final Deaths: \", bedwars.optInt(\"final_deaths_bedwars\")));\n" +
                "        handler.write(strongBr(\"Final K/D: \", getUtils().buildRatio(bedwars.optInt(\"final_kills_bedwars\"), bedwars.optInt(\"final_deaths_bedwars\"))));\n" +
                "        handler.write(strongBr(\"Final Kill / Normal deaths: \", getUtils().buildRatio(bedwars.optInt(\"final_kills_bedwars\"), bedwars.optInt(\"deaths_bedwars\"))));\n";
        for (String s : in.split("\n")) {
            s = s.replace("handler.write(", "items.add(new DisplayLine(").replace(";", ");").replace("strongBr", "bold").replace("<br>", "");
            System.out.println(s);
        }
    }

    public static double getBedwarsLevel(double exp) {
        // first few levels are different
        if (exp < 500) {
            return 0;
        } else if (exp < 1500) {
            return 1;
        } else if (exp < 3500) {
            return 2;
        } else if (exp < 5500) {
            return 3;
        } else if (exp < 9000) {
            return 4;
        }

        exp -= 9000;
        return exp / 5000 + 4;
    }

}
