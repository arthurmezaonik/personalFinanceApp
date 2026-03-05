package br.com.personalFinanceApp.core.repository;

import br.com.personalFinanceApp.core.model.Household;

import java.util.UUID;

public interface HouseholdRepository {
    Household save(Household household);
    Household findById(UUID householdId);
    void delete(Household household);
}
