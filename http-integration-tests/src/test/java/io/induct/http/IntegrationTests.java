package io.induct.http;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.induct.http.testserver.EchoHandler;
import io.induct.http.testserver.TestingHttpServer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @since 28.2.2015
 */
public abstract class IntegrationTests<C extends HttpClient> {

    private static final InputStream EMPTY_STREAM = new ByteArrayInputStream(new byte[0]);
    private static final int MEGABYTE = 1024 * 1024;

    @ClassRule
    public static TestingHttpServer server = new TestingHttpServer();

    @Rule
    public TestName testName = new TestName();

    private C http;

    protected abstract C initializeHttpClient();

    @Before
    public void setUp() throws Exception {
        http = initializeHttpClient();
    }

    @Test
    public void shouldHandleGet() {
        echo(http::get);
    }

    @Test
    public void shouldHandleEchoingPost() {
        echo(http::post);
    }

    @Test
    public void shouldHandleEchoingPut() throws Exception {
        echo(http::put);
    }

    @Test
    public void shouldHandleEchoingHead() throws Exception {
        echo(http::head);
    }

    @Test
    public void shouldHandleEchoingOptions() throws Exception {
        echo(http::options);
    }

    @Test
    public void shouldHandleEchoingDelete() throws Exception {
        echo(http::delete);
    }

    private void echo(Caller caller) {
        Multimap<String, String> params = HashMultimap.create();
        params.put("echo", testName.getMethodName());

        Multimap<String, String> headers = HashMultimap.create();
        String echoHeaderValue = Long.toString(System.currentTimeMillis());
        headers.put(EchoHandler.X_ECHO_HEADER.toString(), echoHeaderValue);

        InputStream body = new ByteArrayInputStream(testName.getMethodName().getBytes());
        try (Response response = caller.execute("http://localhost:9090/echo", params, headers, body)) {
            assertThat(response.getStatusCode(), is(200));
            assertHeader(response, EchoHandler.X_ECHO_HEADER.toString(), echoHeaderValue);
            assertHeader(response, EchoHandler.X_ECHO_PARAM.toString(), testName.getMethodName());
        }
    }

    @Test
    public void shouldHandleMegabyteOfDataDownstreaming() throws Exception {
        random(http::get);
    }

    private void random(Caller caller) throws IOException {
        Multimap<String, String> params = HashMultimap.create();
        params.put("n", Integer.toString(MEGABYTE));
        Multimap<String, String> headers = HashMultimap.create();

        InputStream responseBody = null;
        try (Response response = caller.execute("http://localhost:9090/random", params, headers, EMPTY_STREAM)) {
            assertThat(response.getStatusCode(), is(200));
            Optional<InputStream> responseBodyOpt = response.getResponseBody();
            assertTrue("No response body available, expected 1MB of content", responseBodyOpt.isPresent());
            // the value is carried outside the try-with-resources to test the side effect that body reading still works
            // even when the context has been shut down
            responseBody = responseBodyOpt.get();
        }

        long counter = 0;
        for (int b = responseBody.read(); b != -1 ; b = responseBody.read()) {
            counter++;
        }

        assertEquals("Wrong amount of bytes in response.", MEGABYTE, counter);
    }

    protected static void assertHeader(Response response, String headerName, String headerValue) {
        FluentIterable<String> header = FluentIterable.from(response.getResponseHeaders().get(headerName));
        assertFalse(headerName + " header is not present in response", header.isEmpty());
        assertEquals(1, header.size());
        assertEquals(headerValue, header.first().get());
    }

    private interface Caller {
        Response execute(String url, Multimap<String, String> params, Multimap<String, String> headers, InputStream body);
    }
}
