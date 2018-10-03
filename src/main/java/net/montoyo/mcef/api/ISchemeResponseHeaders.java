package net.montoyo.mcef.api;

public interface ISchemeResponseHeaders {

    void setMimeType(String mt);
    void setStatus(int status);
    void setStatusText(String st);
    void setResponseLength(int len);
    void setRedirectURL(String redirURL);

}
