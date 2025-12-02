package com.legadi.ui.vacations.exception;

public class VacationsBalanceException extends RuntimeException {

    public VacationsBalanceException(String message) {
        super(message);
    }

    public VacationsBalanceException(String message, Throwable th) {
        super(message, th);
    }

    public VacationsBalanceException(String message, Object resource) {
        super(resource != null ? message + ": " + resource : message);
    }

    public VacationsBalanceException(String message, Object resource, Throwable th) {
        super(resource != null ? message + ": " + resource : message, th);
    }
}
