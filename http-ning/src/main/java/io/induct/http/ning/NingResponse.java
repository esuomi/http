package io.induct.http.ning;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.stackoverflow.collections.ByteBufferBackedInputStream;
import com.stackoverflow.guava.CaseInsensitiveForwardingMap;
import io.induct.http.HttpException;
import io.induct.http.Response;
import io.induct.util.concurrent.SyncValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @since 15.2.2015
 */
public class NingResponse implements Response, AsyncHandler<String> {

    private final Logger log = LoggerFactory.getLogger(NingResponse.class);

    private STATE state = STATE.CONTINUE;

    private List<ByteBuffer> responseBody = new LinkedList<>();

    private final SyncValue<Integer> statusCode;
    private final SyncValue<Multimap<String, String>> headers;
    private final SyncValue<InputStream> body;

    private final CountDownLatch readHeaders = new CountDownLatch(1);

    public NingResponse() {
        statusCode = new SyncValue<>();
        headers = new SyncValue<>();
        body = new SyncValue<>();
    }

    @Override
    public void onThrowable(Throwable t) {
        throw new HttpException(t);
    }

    @Override
    public STATE onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
        if (state != STATE.ABORT) {
            statusCode.push(responseStatus.getStatusCode());
        }
        return state;
    }

    @Override
    public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
        readHeaders.await();
        if (state != STATE.ABORT) {
            Multimap<String, String> convertedHeaders = newCaseInsensitiveMultiMap();
            for (Map.Entry<String, List<String>> h : headers.getHeaders().entrySet()) {
                convertedHeaders.putAll(h.getKey(), h.getValue());
            }
            this.headers.push(convertedHeaders);
        }
        return state;
    }

    @Override
    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
        if (state != STATE.ABORT) {
            ByteBuffer part = bodyPart.getBodyByteBuffer();
            responseBody.add(part);
            log.trace("Received response body part of {} bytes", part.capacity());
        }
        return state;
    }

    private ListMultimap<String, String> newCaseInsensitiveMultiMap() {
        return Multimaps.newListMultimap(new CaseInsensitiveForwardingMap<>(Maps.newHashMap()), Lists::newLinkedList);
    }

    @Override
    public String onCompleted() throws Exception {
        if (state != STATE.ABORT) {

            Iterator<InputStream> responseIterators = FluentIterable.from(responseBody)
                    .transform(new Function<ByteBuffer, InputStream>() {
                        @Override
                        public InputStream apply(ByteBuffer input) {
                            return new ByteBufferBackedInputStream(input);
                        }
                    })
                    .iterator();
            Enumeration<InputStream> responseEnumeration = Iterators.asEnumeration(responseIterators);
            this.body.push(new BufferedInputStream(new SequenceInputStream(responseEnumeration)));
        }
        return ""; // we don't care
    }

    @Override
    public int getStatusCode() {
        return statusCode.get();
    }

    @Override
    public Multimap<String, String> getResponseHeaders() {
        allowHeaderReading();
        return headers.get();
    }

    @Override
    public Optional<InputStream> getResponseBody() {
        allowHeaderReading();
        InputStream content = body.get();
        if (content == null) {
            return Optional.absent();
        } else {
            return Optional.of(content);
        }
    }

    @Override
    public void close() {
        state = STATE.ABORT;
    }

    private void allowHeaderReading() {
        if (readHeaders.getCount() > 0) readHeaders.countDown();
    }
}
