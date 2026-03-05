package br.com.personalFinanceApp.core.exceptions;

public class InvalidNameUpdateException extends RuntimeException {
    public InvalidNameUpdateException(String message) {
        super(message);
    }

    public InvalidNameUpdateException() {
        this("Invalid name update.");
    }
}
