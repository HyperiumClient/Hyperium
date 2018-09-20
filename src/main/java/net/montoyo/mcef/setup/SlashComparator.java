package net.montoyo.mcef.setup;

import java.util.Comparator;

final class SlashComparator implements Comparator<String> {

    private Comparator<String> fallback;

    SlashComparator(Comparator<String> fb) {
        fallback = fb;
    }

    @Override
    public int compare(String a, String b) {
        int slashA = 0;
        int slashB = 0;

        for(int i = 0; i < a.length(); i++) { //WARNING: Sub-optimized code here!
            if(a.charAt(i) == '/' || a.charAt(i) == '\\')
                slashA++;
        }

        for(int i = 0; i < b.length(); i++) {
            if(b.charAt(i) == '/' || b.charAt(i) == '\\')
                slashB++;
        }

        int ret = slashB - slashA;
        if(ret == 0 && fallback != null)
            return fallback.compare(a, b);
        else
            return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        else if(obj instanceof SlashComparator) {
            SlashComparator other = (SlashComparator) obj;

            if(fallback == null)
                return other.fallback == null;
            else
                return other.fallback != null && fallback.equals(other.fallback);
        } else
            return false;
    }

}
