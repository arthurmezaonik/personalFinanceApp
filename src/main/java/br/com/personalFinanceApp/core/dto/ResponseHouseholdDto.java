package br.com.personalFinanceApp.core.dto;

import java.util.UUID;

public record ResponseHouseholdDto(
    UUID id,
    String name
) {
}
