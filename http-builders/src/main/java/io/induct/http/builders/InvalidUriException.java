package io.induct.http.builders;

import io.induct.http.HttpException;

/**
 * @since 2016-01-08
 */
public class InvalidUriException extends HttpException {
    public InvalidUriException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
