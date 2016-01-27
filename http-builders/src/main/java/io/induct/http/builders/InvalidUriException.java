package io.induct.http.builders;

import io.induct.http.HttpException;

/**
 * @since 2016-01-08
 */
public class InvalidUriException extends HttpException {
    private static final long serialVersionUID = -1325401102581494340L;

    public InvalidUriException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
