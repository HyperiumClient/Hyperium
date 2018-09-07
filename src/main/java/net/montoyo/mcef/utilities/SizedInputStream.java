package net.montoyo.mcef.utilities;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream with the length data.
 * @author montoyo
 *
 */
public class SizedInputStream extends InputStream {
    
    private InputStream source;
    private long length;
    private long lenCnt;
    
    /**
     * Constructs a new sized input stream.
     * 
     * @param is The original input stream.
     * @param len The estimated length of the data that can be retrieved from is.
     */
    public SizedInputStream(InputStream is, long len) {
        source = is;
        length = len;
        lenCnt = 0;
    }
    
    /**
     * Call this to know the estimated length of this stream.
     * @return The estimated length of the data that can be retrieved from is.
     */
    public long getContentLength() {
        return length;
    }

    @Override
    public int read() throws IOException {
        int data = source.read();
        if(data >= 0)
            lenCnt++;

        return data;
    }
    
    @Override
    public int read(byte[] data) throws IOException {
        int ret = source.read(data);
        if(ret > 0)
            lenCnt += ret;

        return ret;
    }
    
    @Override
    public int read(byte[] data, int off, int len) throws IOException {
        int ret = source.read(data, off, len);
        if(ret > 0)
            lenCnt += ret;

        return ret;
    }
    
    @Override
    public long skip(long s) throws IOException {
        long ret = source.skip(s);
        if(ret > 0)
            lenCnt += ret;

        return ret;
    }
    
    @Override
    public int available() throws IOException {
        return source.available();
    }
    
    @Override
    public void close() throws IOException {
        source.close();
    }
    
    @Override
    public synchronized void mark(int limit) {
        source.mark(limit);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        source.reset();
    }

    public long resetLengthCounter() {
        long cpy = lenCnt;
        lenCnt = 0;

        return cpy;
    }
    
    @Override
    public boolean markSupported() {
        return source.markSupported();
    }

}

