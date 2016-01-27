package io.induct.http.ning;

import com.google.common.collect.Multimap;
import com.ning.http.client.AsyncHttpClient;
import io.induct.http.HttpClient;
import io.induct.http.HttpException;
import io.induct.http.Response;

import java.io.InputStream;
import java.net.URI;
import java.util.function.BiConsumer;

/**
 * @since 15.2.2015
 */
public class NingHttpClient implements HttpClient, AutoCloseable {

    private final AsyncHttpClient client;

    public NingHttpClient(AsyncHttpClient client) {
        this.client = client;
    }

    @Override
    public Response options(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.prepareOptions(uri.toString()));
    }

    @Override
    public Response get(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.prepareGet(uri.toString()));
    }

    @Override
    public Response head(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.prepareHead(uri.toString()));
    }

    @Override
    public Response post(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.preparePost(uri.toString()));
    }

    @Override
    public Response put(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.preparePut(uri.toString()));
    }

    @Override
    public Response delete(URI uri, Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody) {
        return request(params, headers, requestBody, client.prepareDelete(uri.toString()));
    }

    private Response request(Multimap<String, String> params, Multimap<String, String> headers, InputStream requestBody, AsyncHttpClient.BoundRequestBuilder request) {
        try {
            contributeQueryParams(request, params);
            contributeHeaders(request, headers);
            contributeRequestBody(request, requestBody);
            return execute(request);
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP request failed", npe);
        }
    }

    private void contributeQueryParams(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> params) {
        consumeMultimap(params, request::addQueryParam);
    }

    private void contributeHeaders(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> headers) {
        consumeMultimap(headers, request::addHeader);
    }

    private <T, U> void consumeMultimap(Multimap<T, U> multimap, BiConsumer<T, U> consumer) {
        multimap.asMap().forEach((name, values) ->
            values.forEach((value) ->
                consumer.accept(name, value)));
    }

    private void contributeRequestBody(AsyncHttpClient.BoundRequestBuilder request, InputStream requestBody) {
        if (requestBody != null) {
            request.setBody(requestBody);
        }
    }

    private Response execute(AsyncHttpClient.BoundRequestBuilder request) {
        NingResponse ningResponse = new NingResponse();
        request.execute(ningResponse);
        return ningResponse;
    }

    @Override
    public void close() {
        client.close();
    }
}
