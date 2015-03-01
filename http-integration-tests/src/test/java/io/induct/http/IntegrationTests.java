package io.induct.http;

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @since 28.2.2015
 */
public abstract class IntegrationTests<C extends HttpClient> {

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
    public void shouldHandlePost() {
        echo(http::post);
    }

    @Test
    public void shouldHandlePut() throws Exception {
        echo(http::put);
    }

    @Test
    public void shouldHandleHead() throws Exception {
        echo(http::head);
    }

    @Test
    public void shouldHandleOptions() throws Exception {
        echo(http::options);
    }

    @Test
    public void shouldHandleDelete() throws Exception {
        echo(http::delete);
    }

    private void echo(Caller caller) {
        Multimap<String, String> params = HashMultimap.create();
        params.put("echo", testName.getMethodName());

        Multimap<String, String> headers = HashMultimap.create();
        String echoHeaderValue = Long.toString(System.currentTimeMillis());
        headers.put(EchoHandler.X_ECHO_HEADER.toString(), echoHeaderValue);

        byte[] body = "echo testName.getMethodName()".getBytes();
        try (Response response = caller.execute("http://localhost:9090/echo", params, headers, body)) {
            assertThat(response.getStatusCode(), is(200));
            assertHeader(response, EchoHandler.X_ECHO_HEADER.toString(), echoHeaderValue);
            assertHeader(response, EchoHandler.X_ECHO_PARAM.toString(), testName.getMethodName());
        }
    }

    protected static void assertHeader(Response response, String headerName, String headerValue) {
        FluentIterable<String> header = FluentIterable.from(response.getResponseHeaders().get(headerName));
        assertFalse(headerName + " header is not present in response", header.isEmpty());
        assertEquals(1, header.size());
        assertEquals(headerValue, header.first().get());
    }

    private interface Caller {
        Response execute(String url, Multimap<String, String> params, Multimap<String, String> headers, byte[] body);
    }
}
