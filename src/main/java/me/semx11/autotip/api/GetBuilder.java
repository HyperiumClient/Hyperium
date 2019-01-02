package me.semx11.autotip.api;

import me.semx11.autotip.api.request.Request;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

public class GetBuilder {

    private static final String BASE_URL = "https://api.autotip.pro/";

    private final RequestBuilder builder;

    private GetBuilder(Request request) {
        this.builder = RequestBuilder.get().setUri(BASE_URL + request.getType().getEndpoint());
    }

    public static GetBuilder of(Request request) {
        return new GetBuilder(request);
    }

    public GetBuilder addParameter(String key, Object value) {
        this.builder.addParameter(key, String.valueOf(value));
        return this;
    }

    public HttpUriRequest build() {
        return this.builder.build();
    }

}
