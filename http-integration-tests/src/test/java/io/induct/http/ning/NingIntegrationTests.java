package io.induct.http.ning;

import com.ning.http.client.AsyncHttpClient;
import io.induct.http.IntegrationTests;
import org.junit.After;

/**
 * @since 28.2.2015
 */
public class NingIntegrationTests extends IntegrationTests<NingHttpClient> {

    private AsyncHttpClient ningClient;

    @Override
    protected NingHttpClient initializeHttpClient() {
        ningClient = new AsyncHttpClient();
        return new NingHttpClient(ningClient);
    }

    @After
    public void tearDown() throws Exception {
        ningClient.close();
    }
}
