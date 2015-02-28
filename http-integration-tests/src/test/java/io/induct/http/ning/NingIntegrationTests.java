package io.induct.http.ning;

import io.induct.http.IntegrationTests;

/**
 * @since 28.2.2015
 */
public class NingIntegrationTests extends IntegrationTests<NingHttpClient> {

    @Override
    protected NingHttpClient initializeHttpClient() {
        return new NingHttpClient();
    }
}
