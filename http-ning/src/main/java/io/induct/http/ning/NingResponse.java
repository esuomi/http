package io.induct.http.ning;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import io.induct.http.HttpException;
import io.induct.http.Response;
import io.induct.util.concurrent.SyncValue;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 15.2.2015
 */
public class NingResponse implements Response, AsyncHandler<String> {

    private STATE state = STATE.CONTINUE;

    private ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

    private final SyncValue<Integer> statusCode;
    private final SyncValue<Multimap<String, String>> headers;
    private final SyncValue<byte[]> body;

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
    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
        if (state != STATE.ABORT) {
            responseBody.write(bodyPart.getBodyPartBytes());
        }
        return state;
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
            Multimap<String, String> convertedHeaders = MultimapBuilder.hashKeys().arrayListValues().build();
            for (Map.Entry<String, List<String>> h : headers.getHeaders().entrySet()) {
                convertedHeaders.putAll(h.getKey(), h.getValue());
            }
            this.headers.push(convertedHeaders);
        }
        return state;
    }

    @Override
    public String onCompleted() throws Exception {
        if (state != STATE.ABORT) {
            this.body.push(responseBody.toByteArray());
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
    public Optional<byte[]> getResponseBody() {
        allowHeaderReading();
        byte[] content = body.get();
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
