package io.induct.http.ning;

import com.ning.http.client.AsyncHttpClient;
import io.induct.http.IntegrationTests;

/**
 * @since 28.2.2015
 */
public class NingIntegrationTests extends IntegrationTests<NingHttpClient> {

    @Override
    protected NingHttpClient initializeHttpClient() {
        AsyncHttpClient ningClient = new AsyncHttpClient();
        return new NingHttpClient(ningClient);
    }
}
