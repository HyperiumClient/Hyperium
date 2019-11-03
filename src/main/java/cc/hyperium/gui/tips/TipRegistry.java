package cc.hyperium.gui.tips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipRegistry {

    public static final TipRegistry INSTANCE = new TipRegistry();
    private List<String> hyperiumTipArray = new ArrayList<>();

    public void registerTip(String tip) {
        hyperiumTipArray.add(tip);
    }

    public void registerTips(String... tips) {
        hyperiumTipArray.addAll(Arrays.asList(tips));
    }

    public List<String> getTips() {
        return hyperiumTipArray;
    }
}
