package io.induct.util.concurrent;

import com.google.common.base.Supplier;
import io.induct.http.HttpException;

import java.util.concurrent.CountDownLatch;

/**
 * Async-to-sync value helper. Also can be considered to be a single-item blocking producer-consumer abstraction.
 *
 * @since 15.2.2015
 */
public class SyncValue<V> {

    private V value;

    private final CountDownLatch syncLatch = new CountDownLatch(1);
    private final Supplier<V> supplier = new Supplier<V>() {
        @Override
        public V get() {
            try {
                syncLatch.await();
            } catch (InterruptedException e) {
                throw new HttpException("Could not acquire value", e);
            }
            return value;
        }
    };

    public void push(V value) {
        try {
            this.value = value;
        } finally {
            syncLatch.countDown();
        }
    }

    public V get() {
        return supplier.get();
    }
}
