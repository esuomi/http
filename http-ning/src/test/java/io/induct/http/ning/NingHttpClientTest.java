package io.induct.http.ning;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.induct.http.Response;
import org.junit.Ignore;
import org.junit.Test;

public class NingHttpClientTest {

    @Ignore("have to figure out tests...")
    @Test
    public void juuh() throws Exception {
        try (NingHttpClient ning = new NingHttpClient()) {
            Multimap<String, String> empty = HashMultimap.create();
            try (Response response = ning.get("http://www.google.com", empty, empty, null)) {
                System.out.println("status   = " + response.getStatusCode());
                System.out.println("headers  = " + response.getResponseHeaders());
                System.out.println("body     = " + new String(response.getResponseBody()));
            }
        }
    }
}