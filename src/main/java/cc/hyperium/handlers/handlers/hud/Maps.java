package cc.hyperium.handlers.handlers.hud;


import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static final Map<Integer, String> ENCHANTMENT_SHORT_NAME;
    public static final Map<Integer, String> POTION_SHORT_NAME;

    static {
        ENCHANTMENT_SHORT_NAME = new HashMap<Integer, String>() {
            {
                this.put(0, "P");
                this.put(1, "FP");
                this.put(2, "FF");
                this.put(3, "BP");
                this.put(4, "PP");
                this.put(5, "R");
                this.put(6, "AA");
                this.put(7, "T");
                this.put(8, "DS");
                this.put(9, "FW");
                this.put(16, "SH");
                this.put(17, "SM");
                this.put(18, "BoA");
                this.put(19, "KB");
                this.put(20, "FA");
                this.put(21, "L");
                this.put(32, "EFF");
                this.put(33, "ST");
                this.put(34, "UNB");
                this.put(35, "F");
                this.put(48, "POW");
                this.put(49, "PUN");
                this.put(50, "FLA");
                this.put(51, "INF");
                this.put(61, "LoS");
                this.put(62, "LU");
                this.put(70, "MEN");
            }
        };
        POTION_SHORT_NAME = new HashMap<Integer, String>() {
            {
                this.put(1, "Spe");
                this.put(2, "Slo");
                this.put(3, "Has");
                this.put(4, "Fat");
                this.put(5, "Str");
                this.put(6, "Hea");
                this.put(7, "Dmg");
                this.put(8, "Jum");
                this.put(9, "Nau");
                this.put(10, "Reg");
                this.put(11, "Res");
                this.put(12, "Fir");
                this.put(13, "Wat");
                this.put(14, "Inv");
                this.put(15, "Bli");
                this.put(16, "NV");
                this.put(17, "Hun");
                this.put(18, "Wea");
                this.put(19, "Poi");
                this.put(20, "Wit");
                this.put(21, "HBo");
                this.put(22, "Abs");
                this.put(23, "Sat");
                this.put(24, "Glo");
                this.put(25, "Lev");
                this.put(26, "Luc");
                this.put(27, "BLu");
            }
        };
    }
}
