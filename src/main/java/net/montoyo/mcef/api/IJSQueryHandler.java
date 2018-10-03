package net.montoyo.mcef.api;

/**
 * Use this to handle asynchronous JavaScript queries.
 * Queries are sent to Java using var request_id = window.mcefQuery({ request: 'my_request', persistent: false, onSuccess: function(response) {}, onFailure: function(error_code, error_message) {} })
 * Queries may be cancelled using window.mcefCancel(request_id)
 * 
 * @author montoyo
 *
 */
public interface IJSQueryHandler {
    
    /**
     * Handles a JavaScript query. Queries are created using the following JavaScript code:
     * var request_id = window.mcefQuery({ request: 'my_request', persistent: false, onSuccess: function(response) {}, onFailure: function(error_code, error_message) {} })
     *
     * Be aware that this method is NOT called from the main Minecraft thread, and you have to handle synchronisation yourself.
     * 
     * @param b The browser the query has been created from.
     * @param queryId The unique query identifier.
     * @param query The "request" field.
     * @param persistent If the query is persistent or not. If it is, then cb will remain valid when leaving this method.
     * @param cb Use this to answer the query; this means call the JS onSuccess and onFailure functions.
     * @return true if the query was handled.
     */
    boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb);
    
    /**
     * Handles a JavaScript query cancellation. Queries are cancelled using the following JavaScript code:
     * window.mcefCancel(request_id)
     *
     * Be aware that this method is NOT called from the main Minecraft thread, and you have to handle synchronisation yourself.
     * 
     * @param b The browser the query has been cancelled from.
     * @param queryId The unique query identifier given in {@link #handleQuery(IBrowser, long, String, boolean, IJSQueryCallback)}
     */
    void cancelQuery(IBrowser b, long queryId);

}
