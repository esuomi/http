package io.induct.io;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * <p>{@link InputStream} implementation which can be controlled externally to bind together multiple <code>InputStreams</code>
 * in concurrent fashion. Main intended use is chunked content processing.</p>
 *
 * <p><b>This stream will block until {@link #markComplete()} has been called.</b></p>
 *
 * <p>To add a stream to read, call {@link #offer(InputStream)}. To signal end of content, call {@link #markComplete()}.</p>
 *
 * @since 2016-01-04
 */
public class ContinuousInputStream extends InputStream {
    private final Logger log = LoggerFactory.getLogger(ContinuousInputStream.class);

    private static final int END_OF_STREAM = -1;

    private final BlockingQueue<Optional<InputStream>> streams;

    private Availability availability = Availability.HAS_MORE;

    private Optional<InputStream> current = null;

    public ContinuousInputStream() {
        this.streams = new LinkedBlockingQueue<>();
    }

    @Override
    public int available() throws IOException {
        return availability.value;
    }

    public int read() throws IOException {
        return readBytes(single());
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        return readBytes(multiple(bytes, off, len));
    }

    private Function<InputStream, Integer> single() {
        return (stream) -> {
            try {
                return stream.read();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read content", e);
            }
        };
    }

    private Function<InputStream, Integer> multiple(byte[] bytes, int off, int len) {
        return (stream) -> {
            try {
                return stream.read(bytes, off, len);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read content", e);
            }
        };
    }

    public int readBytes(Function<InputStream, Integer> reader) throws IOException {
        try {
            int val = END_OF_STREAM;
            while (val == END_OF_STREAM && availability != Availability.FINISHED) {
                if (current == null) {
                    current = streams.take();
                }
                if (current.isPresent()) {
                    val = reader.apply(current.get());
                    if (val == END_OF_STREAM) {
                        current = null;
                    }
                } else {
                    availability = Availability.FINISHED;
                }
            }
            return val;
        } catch (InterruptedException e) {
            log.warn("Problem during processing", e);
            availability = Availability.FINISHED;
            return END_OF_STREAM;
        }
    }

    /**
     * Marks the stream as complete. Processing will continue until all already received parts have been processed fully.
     */
    public void markComplete() {
        streams.offer(Optional.empty());
    }

    /**
     *
     * @param stream
     * @return
     * @throws NullPointerException Thrown if given stream is null
     * @see BlockingQueue#offer(Object)
     */
    public boolean offer(InputStream stream) {
        Preconditions.checkNotNull(stream, "non-null streams not allowed!");
        return streams.offer(Optional.of(stream));
    }

    @Override
    public void close() throws IOException {
        availability = Availability.FINISHED;
        streams.clear();
        super.close();
    }

    private enum Availability {
        HAS_MORE(1), FINISHED(0);

        private final int value;

        Availability(int value) {
            this.value = value;
        }

    }
}
