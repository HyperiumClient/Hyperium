package net.montoyo.mcef.client;

import net.montoyo.mcef.api.ISchemeResponseHeaders;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;

public class SchemeResponseHeaders implements ISchemeResponseHeaders {

    private final CefResponse response;
    private final IntRef length;
    private final StringRef redirURL;

    public SchemeResponseHeaders(CefResponse r, IntRef l, StringRef url) {
        response = r;
        length = l;
        redirURL = url;
    }

    @Override
    public void setMimeType(String mt) {
        response.setMimeType(mt);
    }

    @Override
    public void setStatus(int status) {
        response.setStatus(status);
    }

    @Override
    public void setStatusText(String st) {
        response.setStatusText(st);
    }

    @Override
    public void setResponseLength(int len) {
        length.set(len);
    }

    @Override
    public void setRedirectURL(String r) {
        redirURL.set(r);
    }

}
