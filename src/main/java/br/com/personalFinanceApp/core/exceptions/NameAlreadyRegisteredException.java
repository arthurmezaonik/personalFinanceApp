package br.com.personalFinanceApp.core.exceptions;

public class NameAlreadyRegisteredException extends RuntimeException {
    public NameAlreadyRegisteredException(String name) {
        super("Name already registered: " + name);
    }
}
