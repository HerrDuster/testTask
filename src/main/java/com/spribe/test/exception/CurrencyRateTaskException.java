package com.spribe.test.exception;

public class CurrencyRateTaskException extends Exception {
    public CurrencyRateTaskException(String message) {
        super(message);
    }

    public CurrencyRateTaskException(Throwable cause) {
        super(cause);
    }
}