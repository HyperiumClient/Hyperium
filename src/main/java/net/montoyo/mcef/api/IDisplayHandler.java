package net.montoyo.mcef.api;

public interface IDisplayHandler {
    
    /**
     * Handle address changes.
     * @param browser The browser generating the event.
     * @param url The new URL.
     */
    void onAddressChange(IBrowser browser, String url);
    
    /**
     * Handle title changes.
     * @param browser The browser generating the event.
     * @param title The new title.
     */
    void onTitleChange(IBrowser browser, String title);
    
    /**
     * Called when the browser is about to display a tooltip.
     *
     * @param browser The browser generating the event.
     * @param text Contains the text that will be displayed in the tooltip.
     */
    void onTooltip(IBrowser browser, String text);
    
    /**
     * Called when the browser receives a status message. 
     *
     * @param browser The browser generating the event.
     * @param value Contains the text that will be displayed in the status message.
     */
    void onStatusMessage(IBrowser browser, String value);

}
