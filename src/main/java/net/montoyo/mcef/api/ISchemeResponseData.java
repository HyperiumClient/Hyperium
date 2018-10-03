package net.montoyo.mcef.api;

public interface ISchemeResponseData {

    byte[] getDataArray();
    int getBytesToRead();
    void setAmountRead(int rd);

}
