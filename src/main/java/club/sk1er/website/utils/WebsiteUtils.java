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
        String in = "  handler.write(\"<h4>Farm Hunt</h4>\");\n" +
                "        handler.write(strongBr(\"Poop collected: \", arcade.optInt(\"poop_collected\")));\n" +
                "        handler.write(strongBr(\"Farm Hunt Wins: \", arcade.optInt(\"wins_farm_hunt\")));\n" +
                "        handler.write(\"<br><h4>Bounty Hunters</h4>\");\n" +
                "        handler.write(strongBr(\"Wins: \", arcade.optInt(\"wins_oneinthequiver\")));\n" +
                "        handler.write(strongBr(\"Deaths: \", arcade.optInt(\"deaths_oneinthequiver\")));\n" +
                "        handler.write(strongBr(\"Bounty Kills: \", arcade.optInt(\"bounty_kills_oneinthequiver\")));\n" +
                "        handler.write(strongBr(\"Kills: \", arcade.optInt(\"kills_oneinthequiver\")));\n" +
                "        handler.write(\"<br><h4>Blocking Dead</h4>\");\n" +
                "        handler.write(strongBr(\"Kills: \", arcade.optInt(\"kills_dayone\")));\n" +
                "        handler.write(strongBr(\"Headshots: \", arcade.optInt(\"headshots_dayone\")));\n" +
                "        handler.write(strongBr(\"Wins: \", arcade.optInt(\"wins_dayone\")));\n" +
                "        handler.write(strongBr(\"Melee Weapon: \", arcade.optString(\"melee_weapon\").toLowerCase().replace(\"_\", \" \")));\n" +
                "        String SOCCOR_FOOTBALL = (handler.optStringHeader(\"Cf-ipcountry\").equalsIgnoreCase(\"US\") || handler.optStringHeader(\"Cf-ipcountry\").equalsIgnoreCase(\"CA\")) ? \"Soccer\" : \"Football\";\n" +
                "        handler.write(\"<br><h4>\" + SOCCOR_FOOTBALL + \"</h4>\");\n" +
                "        handler.write(strongBr(\"Wins: \", arcade.optInt(\"wins_soccer\")));\n" +
                "        handler.write(strongBr(\"Goals: \", arcade.optInt(\"goals_soccer\")));\n" +
                "        handler.write(strongBr(\"Power Kicks: \", arcade.optInt(\"powerkicks_soccer\")));\n" +
                "        handler.write(\"<br><h4>Mini Walls</h4>\");\n" +
                "        handler.write(strongBr(\"Arrows hit: \", arcade.optInt(\"arrows_hit_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Kills: \", arcade.optInt(\"kills_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Final Kills: \", arcade.optInt(\"final_kills_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Wins: \", arcade.optInt(\"wins_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Deaths: \", arcade.optInt(\"deaths_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Wither Damage: \", arcade.optInt(\"wither_damage_mini_walls\")));\n" +
                "        handler.write(strongBr(\"Wither Kills: \", arcade.optInt(\"wither_kills_mini_walls\")));\n" +
                "        handler.write(\"<br>\");\n" +
                "        handler.write(strongBr(\"Hole in the Wall Record : \", arcade.optInt(\"hitw_record_q\")));\n" +
                "        handler.write(strongBr(\"Total Hole in the Walls Rounds: \", arcade.optInt(\"rounds_hole_in_the_wall\")));\n" +
                "        handler.write(strongBr(\"Hypixel Says Rounds: \", arcade.optInt(\"rounds_simon_says\")));\n" +
                "        handler.write(strongBr(\"Hypixel Says Wins: \", arcade.optInt(\"wins_simon_says\")));\n" +
                "        handler.write(strongBr(\"Kills throwout: \", arcade.optInt(\"kills_throw_out\")));\n" +
                "        handler.write(strongBr(\"Death throwout: \", arcade.optInt(\"deaths_throw_out\")));\n" +
                "        handler.write(strongBr(\"Kills dragon wars: \", arcade.optInt(\"kills_dragonwars2\")));\n" +
                "        handler.write(strongBr(\"Wins dragon wars: \", arcade.optInt(\"wins_dragonwars2\")));\n" +
                "        handler.write(strongBr(\"Build Battle wins: \", arcade.optInt(\"wins_buildbattle\")));\n" +
                "        handler.write(strongBr(\"Max Creeper Attack Wave: \", arcade.optInt(\"max_wave\")));\n" +
                "        handler.write(strongBr(\"Party Games 1 wins: \", arcade.optInt(\"wins_party\")));\n" +
                "        handler.write(strongBr(\"Party Games 2 wins: \", arcade.optInt(\"wins_party_2\")));\n" +
                "        handler.write(strongBr(\"Party Games 3 wins: \", arcade.optInt(\"wins_party_3\")));";

        for (String s : in.split("\n")) {
            s = s.replace("handler.write(", "items.add(new DisplayLine(").replace(";", ");").replace("strongBr","bold").replace("<br>","");
            System.out.println(s);
        }
    }
}
