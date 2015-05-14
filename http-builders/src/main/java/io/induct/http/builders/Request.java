package io.induct.http.builders;

import com.google.common.collect.Multimap;
import io.induct.http.HttpClient;
import io.induct.http.Response;

import java.io.InputStream;

/**
 * @since 2015-05-09
 */
public class Request {

    private final HttpClient http;
    private final String url;
    private final Multimap<String, String> headers;
    private final Multimap<String, String> params;
    private final InputStream body;

    Request(HttpClient http, String url, Multimap<String, String> headers, Multimap<String, String> params, InputStream body) {
        this.http = http;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public Response get() {
        return call(http::get);
    }

    public Response post() {
        return call(http::post);
    }

    public Response put() {
        return call(http::put);
    }

    public Response delete() {
        return call(http::delete);
    }

    private Response call(Method m) {
        return m.call(url, params, headers, body);
    }

    private interface Method {
        Response call(String url, Multimap<String, String> params, Multimap<String, String> headers, InputStream body);
    }
}
