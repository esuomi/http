package io.induct.http.ning;

import com.google.common.collect.Multimap;
import com.ning.http.client.AsyncHttpClient;
import io.induct.http.HttpClient;
import io.induct.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 15.2.2015
 */
public class NingHttpClient implements HttpClient, AutoCloseable {

    private final Logger log = LoggerFactory.getLogger(NingHttpClient.class);

    private final AsyncHttpClient client;

    public NingHttpClient() {
        this.client = new AsyncHttpClient();
    }

    @Override
    public Response options(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP OPTIONS {}", url);
        return request(params, requestBody, client.prepareOptions(url));
    }

    @Override
    public Response get(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP GET {}", url);
        return request(params, requestBody, client.prepareGet(url));
    }

    @Override
    public Response head(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP HEAD {}", url);
        return request(params, requestBody, client.prepareHead(url));
    }

    @Override
    public Response post(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP POST {}", url);
        return request(params, requestBody, client.preparePost(url));
    }

    @Override
    public Response put(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP PUT {}", url);
        return request(params, requestBody, client.preparePut(url));
    }

    @Override
    public Response delete(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP DELETE {}", url);
        return request(params, requestBody, client.prepareDelete(url));
    }

    @Override
    public Response connect(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody) {
        log.debug("HTTP CONNECT {}", url);
        return request(params, requestBody, client.prepareConnect(url));
    }

    private Response request(Multimap<String, String> params, byte[] requestBody, AsyncHttpClient.BoundRequestBuilder request) {
        contributeQueryParams(request, params);
        contributeHeaders(request, params);
        if (requestBody != null) {
            log.debug("\t---");
            log.debug("\trequest body: {} bytes", requestBody.length);
            request.setBody(requestBody);
        }
        NingResponse ningResponse = new NingResponse();
        request.execute(ningResponse);
        return ningResponse;
    }

    private void contributeQueryParams(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> params) {
        for (Map.Entry<String, Collection<String>> param : params.asMap().entrySet()) {
            boolean first = true;
            for (String value : param.getValue()) {
                if (log.isDebugEnabled()) {
                    String delim;
                    if (first) {
                        delim = "?";
                        first = false;
                    } else {
                        delim = "&";
                    }
                    log.debug("\t{}{}={}", delim, param.getKey(), value);
                }
                request.addQueryParam(param.getKey(), value);
            }
        }
    }

    private void contributeHeaders(AsyncHttpClient.BoundRequestBuilder request, Multimap<String, String> headers) {
        for (Map.Entry<String, Collection<String>> header : headers.asMap().entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                log.debug("\trequest header >> {}:{}", name, value);
                request.addHeader(name, value);
            }
        }
    }

    @Override
    public void close() {
        client.close();
    }
}
