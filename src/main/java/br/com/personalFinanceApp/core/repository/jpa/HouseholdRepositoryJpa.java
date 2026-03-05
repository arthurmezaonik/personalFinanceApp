package br.com.personalFinanceApp.core.repository.jpa;

import br.com.personalFinanceApp.core.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HouseholdRepositoryJpa extends JpaRepository<Household, UUID> {

}
