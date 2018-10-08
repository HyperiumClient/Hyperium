package net.montoyo.mcef.api;

public interface API {
    
    /**
     * Creates a web view and loads the specified URL.
     * 
     * @param url The URL to start from.
     * @param transp True is the web view can be transparent
     * @return The created web view, or null if this is run on server.
     */
    IBrowser createBrowser(String url, boolean transp);

    /**
     * Same as {@link #createBrowser(String, boolean) createBrowser} but with transp set to false.
     *
     * @param url The URL to start from.
     * @return The created web view, or null if this is run on server.
     */
    IBrowser createBrowser(String url);
    
    /**
     * Registers a display handler.
     * @param idh The display handler to register.
     * @see IDisplayHandler
     */
    void registerDisplayHandler(IDisplayHandler idh);
    
    /**
     * Registers a JavaScript query handler.
     * @param iqh The JavaScript query handler to register.
     * @see IJSQueryHandler
     */
    void registerJSQueryHandler(IJSQueryHandler iqh);
    
    /**
     * Call this to know if MCEF is in virtual mode.
     * MCEF switches in virtual mode if something failed to load.
     * When in virtual mode, {@link #createBrowser(String, boolean) createBrowser} will generate fake browsers that does nothing.
     * 
     * @return true if MCEF is in virtual mode.
     */
    boolean isVirtual();
    
    /**
     * Opens the example browser UI.
     * @param url The URL to load.
     * @see net.montoyo.mcef.example.ExampleMod
     */
    void openExampleBrowser(String url);

    /**
     * Returns a mime type from a file extension,
     * or null, if there is no mapping for this extension.
     *
     * @param ext File extension, without the '.'
     * @return A mime type corresponding to this extension, or null if none could be found.
     */
    String mimeTypeFromExtension(String ext);

    /**
     * Registers a scheme (custom URLs).
     * This has to be done in preInit, init happens too late.
     *
     * @param name The name of the scheme
     * @param schemeClass The class that will be instantiated for request
     * @param std Whether the scheme has standards URLs, like "protocol://host/path". Non standard is just "procotol:"
     * @param local If the scheme is local, some special security rules are applied, just like the "file:///" scheme.
     * @param displayIsolated iframes (and such things) from an external scheme cannot access pages from this scheme.
     * @see org.cef.callback.CefSchemeRegistrar
     */
    void registerScheme(String name, Class<? extends IScheme> schemeClass, boolean std,
        boolean local, boolean displayIsolated);

    /**
     * Checks whether the scheme with name 'name' is already registered.
     *
     * @param name The name of the scheme
     * @return true if it is registered, false otherwise.
     */
    boolean isSchemeRegistered(String name);

}
