package io.induct.util.concurrent;

/**
 * Thrown whenever running process, action or any equivalent is interrupted, cancelled or otherwise prevented from
 * finishing successfully.
 *
 * @since 1.3.2015
 */
public class HaltedException extends RuntimeException {
    public HaltedException(String message, Throwable cause) {
        super(message, cause);
    }
}
