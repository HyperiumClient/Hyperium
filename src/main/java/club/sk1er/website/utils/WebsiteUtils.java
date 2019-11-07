package club.sk1er.website.utils;

import cc.hyperium.Hyperium;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class WebsiteUtils {

    public static final List<String> blitz_kits = new ArrayList<>();
    public static final TreeMap<Integer, String> numerals = new TreeMap<>();

    static {
        numerals.put(100, "C");
        numerals.put(90, "XC");
        numerals.put(50, "L");
        numerals.put(40, "XL");
        numerals.put(10, "X");
        numerals.put(9, "IX");
        numerals.put(5, "V");
        numerals.put(4, "IV");
        numerals.put(1, "I");
        blitz_kits.add("Archer");
        blitz_kits.add("Arachnologist");
        blitz_kits.add("Astronaut");
        blitz_kits.add("Armorer");
        blitz_kits.add("Creeper Tamer");
        blitz_kits.add("Baker");
        blitz_kits.add("Blaze");
        blitz_kits.add("Farmer");
        blitz_kits.add("Fisherman");
        blitz_kits.add("Florist");
        blitz_kits.add("Golem");
        blitz_kits.add("Horsetamer");
        blitz_kits.add("Hunter");
        blitz_kits.add("Knight");
        blitz_kits.add("Meat Master");
        blitz_kits.add("Necromancer");
        blitz_kits.add("Paladin");
        blitz_kits.add("Pigman");
        blitz_kits.add("Red Dragon");
        blitz_kits.add("Reaper");
        blitz_kits.add("Rogue");
        blitz_kits.add("Scout");
        blitz_kits.add("Shadow Knight");
        blitz_kits.add("Slimey Slime");
        blitz_kits.add("Snowman");
        blitz_kits.add("Speleologist");
        blitz_kits.add("Tim");
        blitz_kits.add("Toxicologist");
        blitz_kits.add("Troll");
        blitz_kits.add("Wolftamer");
        blitz_kits.add("Hype Train");
        blitz_kits.add("Jockey");

    }

    public static String numeral(int amount) {
        try {
            int l = numerals.floorKey(amount);
            if (l == amount) return numerals.get(l);
            return numerals.get(l) + numeral(amount - l);
        } catch (Exception e) {
            return "-";
        }
    }

    public static String comma(Number number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    public static String buildRatio(int a, int b) {
        if (a + b == 0) return "0";
        if (b == 0) return Character.toString('\u221e');
        if (a == 0) return "0";
        double e = (double) a / (double) b;
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
                    if (cur >= max - 1) return (curent.has(s) ? curent.get(s).getAsLong() : 0);
                    else curent = curent.has(s) ? curent.get(s).getAsJsonObject() : new JsonObject();
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
            s = s.replace("handler.write(",
                "items.add(new DisplayLine(").replace(";", ");").replace("strongBr", "bold").replace("<br>", "");
            Hyperium.LOGGER.debug(s);
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
