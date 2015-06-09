package com.dch.app.analyst;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public class AnalystException extends RuntimeException {

    public AnalystException(String message) {
        super(message);
    }

    public AnalystException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalystException(Throwable cause) {
        super(cause);
    }
}
