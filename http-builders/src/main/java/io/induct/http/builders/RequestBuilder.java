package io.induct.http.builders;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.induct.http.HttpClient;
import io.mikael.urlbuilder.UrlBuilder;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Builder class for {@link Request} instances. Can be reused to create multiple executable {@code Request} instances,
 * all {@code with*(...)} methods return a new isolated instance.
 *
 * @since 2015-05-09
 */
public class RequestBuilder {

    private final HttpClient httpClient;

    private final URI uri;

    private final Multimap<String, String> headers;

    private final Multimap<String, String> params;

    private final Optional<InputStream> body;

    public RequestBuilder(HttpClient httpClient) {
        this(httpClient, null, HashMultimap.create(), HashMultimap.create(), Optional.empty());
    }

    private RequestBuilder(HttpClient httpClient,
                           URI uri,
                           Multimap<String, String> headers,
                           Multimap<String, String> params,
                           Optional<InputStream> body) {
        this.httpClient = httpClient;
        this.uri = uri;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public RequestBuilder withUri(String uri) {
        try {
            URI parsedUri = UrlBuilder.fromString(uri).toUriWithException();
            Preconditions.checkArgument(parsedUri.getQuery() == null, "URI should not contain query params! Set params by calling #withParams(...)");
            return new RequestBuilder(httpClient, parsedUri, HashMultimap.create(headers), HashMultimap.create(params), body);
        } catch (URISyntaxException e) {
            throw new InvalidUriException("Invalid URL, cannot build request from " + uri, e);
        }
    }

    public RequestBuilder withHeaders(Consumer<Multimap<String, String>> headerContributor) {
        Multimap<String, String> newHeaders = HashMultimap.create(headers);
        headerContributor.accept(newHeaders);
        return new RequestBuilder(httpClient, uri, newHeaders, HashMultimap.create(params), body);
    }

    public RequestBuilder withParams(Consumer<Multimap<String, String>> paramContributor) {
        Multimap<String, String> newParams = HashMultimap.create(params);
        paramContributor.accept(newParams);
        return new RequestBuilder(httpClient, uri, HashMultimap.create(headers), newParams, body);
    }

    public RequestBuilder withBody(Optional<InputStream> newBody) {
        return new RequestBuilder(httpClient, uri, HashMultimap.create(headers), HashMultimap.create(params), newBody);
    }

    public Request build() {
        return new Request(httpClient, uri, HashMultimap.create(headers), HashMultimap.create(params), body);
    }
}
