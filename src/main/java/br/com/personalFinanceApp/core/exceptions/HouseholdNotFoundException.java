package br.com.personalFinanceApp.core.exceptions;

public class HouseholdNotFoundException extends RuntimeException {
    public HouseholdNotFoundException(String message) {
        super(message);
    }

    public HouseholdNotFoundException() {
        super("Household not found.");
    }
}
