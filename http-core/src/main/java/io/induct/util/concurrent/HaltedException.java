package io.induct.util.concurrent;

/**
 * Thrown whenever running process, action or any equivalent is interrupted, cancelled or otherwise prevented from
 * finishing successfully.
 *
 * This exception is similar to {@link java.util.concurrent.CancellationException} but provides exception chaining
 * constructors.
 *
 * @since 1.3.2015
 */
public class HaltedException extends RuntimeException {

    public HaltedException() {
        super();
    }

    public HaltedException(String message) {
        super(message);
    }

    public HaltedException(Throwable cause) {
        super(cause);
    }

    public HaltedException(String message, Throwable cause) {
        super(message, cause);
    }
}
