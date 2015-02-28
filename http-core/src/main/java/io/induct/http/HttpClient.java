package io.induct.http;

import com.google.common.collect.Multimap;

/**
 * Base HTTP client abstraction with the most commonly used and useful methods. <code>TRACE</code> and <code>CONNECT</code>
 * are omitted as they are not generally used for notmal HTTP interaction.
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
