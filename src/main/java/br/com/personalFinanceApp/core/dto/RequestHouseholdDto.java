package br.com.personalFinanceApp.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestHouseholdDto(
    @NotNull(message = "Name field is required.")
    @Size(min = 1, max = 255, message = "Name size needs to be between 1 and 255 characters.")
    String name
) {
}
