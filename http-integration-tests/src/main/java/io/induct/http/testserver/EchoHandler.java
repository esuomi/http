package io.induct.http.testserver;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.streams.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @since 28.2.2015
 */
public class EchoHandler implements HttpHandler {

    private final Logger log = LoggerFactory.getLogger(EchoHandler.class);

    public final static HttpString X_ECHO_HEADER = HttpString.tryFromString("X-Echo-Header");
    public final static HttpString X_ECHO_PARAM = HttpString.tryFromString("X-Echo-Param");

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        transferEchoHeader(exchange);
        transferEchoBody(exchange);
    }

    private void transferEchoHeader(HttpServerExchange exchange) {
        HeaderValues echoHeaderValues = exchange.getRequestHeaders().get(X_ECHO_HEADER);
        exchange.getResponseHeaders().addAll(X_ECHO_HEADER, echoHeaderValues);
        exchange.getResponseHeaders().addAll(X_ECHO_PARAM, exchange.getQueryParameters().get("echo"));
    }

    private void transferEchoBody(HttpServerExchange exchange) {
        exchange.startBlocking();
        try (InputStream in = exchange.getInputStream();
            OutputStream out = exchange.getOutputStream()) {
            Streams.copyStream(in, out);
        } catch (IOException e) {
            log.warn("Failed to copy request body to response body", e);
        }
    }
}
