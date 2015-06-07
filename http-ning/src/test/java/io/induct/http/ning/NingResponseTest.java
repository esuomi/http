package io.induct.http.ning;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @since 2015-05-23
 */
public class NingResponseTest {

    @Test
    public void shouldReturnEmptyValuesIfConnectionFails() throws Exception {
        NingResponse response = new NingResponse();

        response.onThrowable(new RuntimeException("simulated failure"));

        assertTrue(response.getResponseHeaders().isEmpty());
        assertFalse(response.getResponseBody().isPresent());
        assertEquals(NingResponse.INVALID_STATUS_CODE, response.getStatusCode());
    }
}