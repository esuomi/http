package io.induct.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

/**
 * @since 2016-01-04
 */
public class ContinuousInputStreamTest {

    @Test
    public void dataComesAvailableAfterPushingStream() throws Exception {

        ContinuousInputStream stream = new ContinuousInputStream();
        Thread t = new Thread(() -> {
            stream.offer(new ByteArrayInputStream("Hello, ".getBytes()));
            stream.offer(new ByteArrayInputStream("World!".getBytes()));
            stream.markComplete();
        });

        try {
            t.start();
            StringBuilder greeting = new StringBuilder();
            while (stream.available() > 0) {
                int c = stream.read();
                if (c > -1) {
                    greeting.append((char) c);
                }
            }

            assertEquals("Hello, World!", greeting.toString());
        } finally {
            stream.close();
            t.interrupt();
        }
    }
}
