package net.montoyo.mcef.api;

public interface IScheme {

    SchemePreResponse processRequest(String url);
    void getResponseHeaders(ISchemeResponseHeaders resp);
    boolean readResponse(ISchemeResponseData data);

}
