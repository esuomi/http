package com.stackoverflow.collections;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Slightly modified ByteBuffer InputStream wrapper from Stack Overflow with construction preconditions.
 *
 * @see <a href="http://stackoverflow.com/a/6603018/44523">Wrapping a ByteBuffer with an InputStream</a>
 * @since 3.3.2015
 */
public class ByteBufferBackedOutputStream extends OutputStream {

    private final ByteBuffer buf;

    public ByteBufferBackedOutputStream(ByteBuffer buf) {
        Preconditions.checkNotNull(buf, "Can not construct stream from null buffer");
        this.buf = buf;
    }

    public void write(int b) throws IOException {
        buf.put((byte) b);
    }

    public void write(byte[] bytes, int off, int len)
            throws IOException {
        buf.put(bytes, off, len);
    }

}
