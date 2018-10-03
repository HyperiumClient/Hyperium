package net.montoyo.mcef.setup;

import java.util.Comparator;

final class DefaultComparator implements Comparator<String> {

    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof DefaultComparator;
    }

}
