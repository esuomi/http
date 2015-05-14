package io.induct.http.builders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.induct.http.HttpClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Builder class for {@link Request} instances. Can be reused to create multiple executable {@code Request} instances,
 * all {@code with*(...)} methods return a new isolated instance.
 *
 * @since 2015-05-09
 */
public class RequestBuilder {

    private static final InputStream EMPTY_BODY = new ByteArrayInputStream(new byte[0]);

    private final HttpClient httpClient;

    private final String url;

    private final Multimap<String, String> headers;

    private final Multimap<String, String> params;

    private final InputStream body;

    public RequestBuilder(HttpClient httpClient) {
        this(httpClient, null, HashMultimap.create(), HashMultimap.create(), EMPTY_BODY);
    }

    private RequestBuilder(HttpClient httpClient,
                           String url,
                           Multimap<String, String> headers,
                           Multimap<String, String> params,
                           InputStream body) {
        this.httpClient = httpClient;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public RequestBuilder withUrl(String url) {
        return new RequestBuilder(httpClient, url, HashMultimap.create(headers), HashMultimap.create(params), body);
    }

    public RequestBuilder withHeaders(Consumer<Multimap<String, String>> headerContributor) {
        Multimap<String, String> newHeaders = HashMultimap.create(headers);
        headerContributor.accept(newHeaders);
        return new RequestBuilder(httpClient, url, newHeaders, HashMultimap.create(params), body);
    }

    public RequestBuilder withParams(Consumer<Multimap<String, String>> paramContributor) {
        Multimap<String, String> newParams = HashMultimap.create(params);
        paramContributor.accept(newParams);
        return new RequestBuilder(httpClient, url, HashMultimap.create(headers), newParams, body);
    }

    public RequestBuilder withBody(InputStream newBody) {
        return new RequestBuilder(httpClient, url, HashMultimap.create(headers), HashMultimap.create(params), newBody);
    }

    public Request build() {
        // TODO: Validate url
        return new Request(httpClient, url, HashMultimap.create(headers), HashMultimap.create(params), body);
    }
}
