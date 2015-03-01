package io.induct.http.ning;

import com.ning.http.client.AsyncHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NingHttpClientTest {
    
    private NingHttpClient httpClient;

    @Mock
    private AsyncHttpClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        httpClient = new NingHttpClient(client);
    }

    @Test
    public void a() throws Exception {


    }
}