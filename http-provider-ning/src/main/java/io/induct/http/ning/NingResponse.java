package io.induct.http.ning;

import com.google.common.collect.HashMultimap;
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
import io.induct.http.Response;
import io.induct.io.ContinuousInputStream;
import io.induct.util.concurrent.SyncValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @since 15.2.2015
 */
public class NingResponse implements Response, AsyncHandler<Void> {

    public static final int INVALID_STATUS_CODE = -1;

    private final Logger log = LoggerFactory.getLogger(NingResponse.class);

    private STATE state = STATE.CONTINUE;

    private final SyncValue<Integer> statusCode;
    private final SyncValue<Multimap<String, String>> headers;

    private final ContinuousInputStream body;

    private final CountDownLatch readHeaders = new CountDownLatch(1);

    public NingResponse() {
        statusCode = new SyncValue<>();
        headers = new SyncValue<>();
        body = new ContinuousInputStream();
    }

    @Override
    public void onThrowable(Throwable t) {
        state = STATE.ABORT;

        if (!statusCode.isAssigned())
            statusCode.push(INVALID_STATUS_CODE);

        if (!headers.isAssigned())
            headers.push(HashMultimap.create());

        body.markComplete();
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
        ByteBuffer part = bodyPart.getBodyByteBuffer();
        log.debug("Received response body part of {} bytes", part.limit());
        if (body.offer(new ByteBufferBackedInputStream(part))) {
            log.debug("Body part was added to continuous body stream");
        } else {
            log.warn("Body stream rejected body part");
        }
        log.debug("is last? {} closing underlying? {}", bodyPart.isLast(), bodyPart.isUnderlyingConnectionToBeClosed());
        return state;
    }

    private ListMultimap<String, String> newCaseInsensitiveMultiMap() {
        return Multimaps.newListMultimap(new CaseInsensitiveForwardingMap<>(Maps.newHashMap()), Lists::newLinkedList);
    }

    @Override
    public Void onCompleted() throws Exception {
        log.info("Processing complete");
        body.markComplete();
        return null; // we don't care
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
    public InputStream getResponseBody() {
        allowHeaderReading();
        return body;
    }

    @Override
    public void close() {
        state = STATE.ABORT;
    }

    private void allowHeaderReading() {
        if (readHeaders.getCount() > 0) readHeaders.countDown();
    }
}
