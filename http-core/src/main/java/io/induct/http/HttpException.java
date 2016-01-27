package io.induct.http;

/**
 * Generic HTTP related exception. Used whenever an actual HTTP related problem is thrown; this excludes parameter
 * formatting and other similar preconditional situations.
 *
 * @since 15.2.2015
 */
public class HttpException extends RuntimeException {

    private static final long serialVersionUID = 4157677754810080456L;

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
