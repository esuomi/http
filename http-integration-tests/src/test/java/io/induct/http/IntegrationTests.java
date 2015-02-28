package io.induct.http;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.induct.http.testserver.TestingHttpServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @since 28.2.2015
 */
public abstract class IntegrationTests<C extends HttpClient> {

    private C http;

    @Before
    public void setUp() throws Exception {
        http = initializeHttpClient();
    }

    protected abstract C initializeHttpClient();

    @Rule
    public TestingHttpServer server = new TestingHttpServer();

    @Test
    public void shouldHandleHttpGet() throws Exception {
        Multimap<String, String> params = HashMultimap.create();
        Multimap<String, String> headers = HashMultimap.create();

        String echoHeaderValue = Long.toString(System.currentTimeMillis());
        headers.put("X-Echo", echoHeaderValue);

        byte[] body = "echo".getBytes();

        try (Response response = http.get("http://localhost:9090/echo", params, headers, body)) {
            assertThat(response.getStatusCode(), is(200));

            FluentIterable<String> echoHeader = FluentIterable.from(response.getResponseHeaders().get("X-Echo"));
            assertFalse("No X-Echo header present in response", echoHeader.isEmpty());
            assertEquals(1, echoHeader.size());
            assertEquals(echoHeaderValue, echoHeader.first().get());
        }

    }
}
