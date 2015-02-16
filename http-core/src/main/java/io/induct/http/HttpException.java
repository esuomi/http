package io.induct.http;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 15.2.2015
 */
public class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

}
