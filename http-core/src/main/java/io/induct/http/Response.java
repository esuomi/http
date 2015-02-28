package io.induct.http;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

/**
 * @since 15.2.2015
 */
public interface Response extends AutoCloseable {

    /**
     * Access response status code. Abstraction for making status codes human readable is not provided as tons of
     * libraries provide such already.
     *
     * @return HTTP status code of the response
     */
    int getStatusCode();

    /**
     * Access response headers. As with request headers in the request APIs of {@link io.induct.http.HttpClient}, the
     * returned headers are assumed to be a multimap as it is possible to respond with, for example, multiple
     * <code>Set-Cookie</code> headers.
     *
     * @return Response headers
     */
    Multimap<String, String> getResponseHeaders();

    /**
     * Access response body if one is available. The response body can be expected to be the whole body, not multipart
     * or stream.
     *
     * @return Optionally available response body.
     */
    Optional<byte[]> getResponseBody();

    /**
     * Allow the use of <code>try-with-resources</code> block to only read the minimal amount of data. Exact behavior is
     * implementation specific and is considered optional to implement.
     */
    @Override
    void close();
}
