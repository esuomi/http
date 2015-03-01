package io.induct.http;

import com.google.common.collect.Multimap;

/**
 * Low level HTTP client abstraction for interacting with HTTP APIs or alternatively functional test derivatives. The
 * signature of this interface is to promote simplicity with testing in mind, whether it's hot swapping clients, mocking
 * in tests or wrapping a framework specific testing library.
 *
 * All implementations are to be used by utilising <code>try-with-resources</code> blocks:
 *<pre><code>try (Response response = httpClient.{method}(url, params, headers, body) {
 *  // work with the values from response
 *}</code></pre>
 * The {@link io.induct.http.Response} interface is designed to allow implementations to halt processing of the incoming
 * response at any time.
 *
 * <code>TRACE</code> and <code>CONNECT</code> are omitted as they are not generally used for normal HTTP interaction.
 *
 * @since 15.2.2015
 */
public interface HttpClient {

    Response options(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response get(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response head(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response post(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response put(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response delete(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);

}
