package io.induct.http;

import com.google.common.collect.Multimap;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * Low level HTTP client abstraction for interacting with HTTP APIs or alternatively functional test derivatives. The
 * signature of this interface is to promote simplicity with testing in mind, whether it's hot swapping clients, mocking
 * in tests or wrapping a framework specific testing library.
 *
 * All implementations are to be used by utilising `try-with-resources` blocks:
 *
 * ```java
 * try (Response response = httpClient.{method}(url, params, headers, body) {
 *   // work with the values from response
 * }
 * ```
 *
 * The {@link io.induct.http.Response} interface is designed to allow implementations to halt processing of the incoming
 * response at any time.
 *
 * `TRACE`and `CONNECT` are omitted as they are not generally used for normal HTTP interaction.
 *
 * @since 15.2.2015
 */
public interface HttpClient {

    /**
     * Perform HTTP OPTIONS call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send. Note that HTTP OPTIONS does not specify any use for this in HTTP1.1 spec
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.2">RFC-2616 9.2 OPTIONS</a>
     */
    Response options(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

    /**
     * Perform HTTP GET call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.3">RFC-2616 9.3 GET</a>
     */
    Response get(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

    /**
     * Perform HTTP HEAD call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.4">RFC-2616 9.4 HEAD</a>
     */
    Response head(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

    /**
     * Perform HTTP POST call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.5">RFC-2616 9.5 POST</a>
     */
    Response post(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

    /**
     * Perform HTTP PUT call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.6">RFC-2616 9.6 PUT</a>
     */
    Response put(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

    /**
     * Perform HTTP DELETE call.
     *
     * @param uri Full URL including scheme, authority and path
     * @param params URL parameters for the call
     * @param headers HTTP headers to send with the call
     * @param requestBody Request body to send
     * @return Server response
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-9.7">RFC-2616 9.7 DELETE</a>
     */
    Response delete(URI uri, Multimap<String, String> params, Multimap<String, String> headers, Optional<InputStream> requestBody);

}
