package io.induct.http;

import com.google.common.collect.Multimap;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 15.2.2015
 */
public interface HttpClient {

    Response options(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response get(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response head(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response post(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response put(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response delete(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);
    Response connect(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] requestBody);

}
