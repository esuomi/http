package io.induct.http;

import com.google.common.collect.Multimap;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 15.2.2015
 */
public interface Response extends AutoCloseable {

    int getStatusCode();

    Multimap<String, String> getResponseHeaders();

    byte[] getResponseBody();

    /**
     * Allow the use of <code>try-with-resources</code> block to only read the minimal amount of data. Exact behavior is
     * implementation specific and is considered optional to implement.
     */
    @Override
    void close();
}
