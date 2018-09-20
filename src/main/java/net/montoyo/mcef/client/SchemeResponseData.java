package net.montoyo.mcef.client;

import net.montoyo.mcef.api.ISchemeResponseData;
import org.cef.misc.IntRef;

public class SchemeResponseData implements ISchemeResponseData {

    private final byte[] data;
    private final int toRead;
    private final IntRef read;

    public SchemeResponseData(byte[] data, int toRead, IntRef read) {
        this.data = data;
        this.toRead = toRead;
        this.read = read;
    }

    @Override
    public byte[] getDataArray() {
        return data;
    }

    @Override
    public int getBytesToRead() {
        return toRead;
    }

    @Override
    public void setAmountRead(int rd) {
        read.set(rd);
    }

}
