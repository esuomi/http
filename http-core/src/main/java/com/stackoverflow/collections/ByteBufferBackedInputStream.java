package com.stackoverflow.collections;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Slightly modified ByteBuffer InputStream wrapper from Stack Overflow with construction preconditions.
 *
 * @see <a href="http://stackoverflow.com/a/6603018/44523">Wrapping a ByteBuffer with an InputStream</a>
 * @since 3.3.2015
 */
public class ByteBufferBackedInputStream extends InputStream {

    private final ByteBuffer buf;

    public ByteBufferBackedInputStream(ByteBuffer buf) {
        Preconditions.checkNotNull(buf, "Can not construct stream from null buffer");
        this.buf = buf;
    }

    public int read() throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }
        return buf.get() & 0xFF;
    }

    public int read(byte[] bytes, int off, int len)
            throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }

        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        return len;
    }
}
