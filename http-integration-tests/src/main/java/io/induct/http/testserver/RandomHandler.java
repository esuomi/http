package io.induct.http.testserver;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Random;

/**
 * @since 3.3.2015
 */
public class RandomHandler implements HttpHandler {

    private final Logger log = LoggerFactory.getLogger(RandomHandler.class);

    private final Random r = new Random();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        generateRandomResponseBytes(exchange);
    }

    private void generateRandomResponseBytes(HttpServerExchange exchange) throws IOException {
        Deque<String> amounts = exchange.getQueryParameters().get("n");
        if (!amounts.isEmpty()) {
            int amount = Integer.valueOf(amounts.getFirst());
            log.debug("Will generate {} random bytes to output", amount);
            byte[] bytes = new byte[amount];
            r.nextBytes(bytes);
            exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
        }
    }
}
