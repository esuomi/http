package io.induct.http.ning;

import com.google.common.collect.Multimap;
import com.ning.http.client.AsyncHttpClient;
import io.induct.http.HttpClient;
import io.induct.http.HttpException;
import io.induct.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @since 15.2.2015
 */
public class NingHttpClient implements HttpClient, AutoCloseable {

    private final Logger log = LoggerFactory.getLogger(NingHttpClient.class);

    private final AsyncHttpClient client;

    public NingHttpClient(AsyncHttpClient client) {
        this.client = client;
    }

    @Override
    public Response options(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP OPTIONS {}", url);
        try {
            return request(params, headers, requestBody, client.prepareOptions(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP OPTIONS failed", npe);
        }
    }

    @Override
    public Response get(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP GET {}", url);
        try {
            return request(params, headers, requestBody, client.prepareGet(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP GET failed", npe);
        }
    }

    @Override
    public Response head(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP HEAD {}", url);
        try {
            return request(params, headers, requestBody, client.prepareHead(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP HEAD failed", npe);
        }
    }

    @Override
    public Response post(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP POST {}", url);
        try {
            return request(params, headers, requestBody, client.preparePost(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP POST failed", npe);
        }
    }

    @Override
    public Response put(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP PUT {}", url);
        try {
            return request(params, headers, requestBody, client.preparePut(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP PUT failed", npe);
        }
}

    @Override
    public Response delete(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP DELETE {}", url);
        try {
            return request(params, headers, requestBody, client.prepareDelete(url));
        } catch (NullPointerException npe) {
            throw new HttpException("HTTP DELETE failed", npe);
        }
    }

    private Response request(Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody, AsyncHttpClient.BoundRequestBuilder request) {
        contributeQueryParams(request, params);
        contributeHeaders(request, headers);
        contributeRequestBody(request, requestBody);
        return execute(request);
    }

    private void contributeQueryParams(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> params) {
        log.debug("\t\tparams: {}", params);
        for (Map.Entry<String, Collection<String>> param : params.asMap().entrySet()) {
            for (String value : param.getValue()) {
                request.addQueryParam(param.getKey(), value);
            }
        }
    }

    private void contributeHeaders(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> headers) {
        for (Map.Entry<String, Collection<String>> header : headers.asMap().entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                log.debug("\t\trequest header >> {}:{}", name, value);
                request.addHeader(name, value);
            }
        }
    }

    private void contributeRequestBody(AsyncHttpClient.BoundRequestBuilder request, byte[] requestBody) {
        if (requestBody != null) {
            log.debug("\t\trequest body: {} bytes", requestBody.length);
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
