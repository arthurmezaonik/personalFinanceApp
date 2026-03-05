package br.com.personalFinanceApp.core.dto;

public record ErrorResponseDto(
    int status,
    String error,
    String message
) {
}
