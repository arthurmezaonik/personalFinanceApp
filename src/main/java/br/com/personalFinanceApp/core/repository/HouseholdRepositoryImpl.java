package br.com.personalFinanceApp.core.repository;

import br.com.personalFinanceApp.core.exceptions.HouseholdNotFoundException;
import br.com.personalFinanceApp.core.model.Household;
import br.com.personalFinanceApp.core.repository.jpa.HouseholdRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class HouseholdRepositoryImpl implements HouseholdRepository {

    private final HouseholdRepositoryJpa householdRepository;

    public HouseholdRepositoryImpl(HouseholdRepositoryJpa householdRepository) {
        this.householdRepository = householdRepository;
    }

    @Override
    public Household save(Household newHousehold){
        return householdRepository.save(newHousehold);
    }

    @Override
    public Household findById(UUID householdId) {
        return getHouseholdModel(householdRepository.findById(householdId));
    }

    @Override
    public void delete(Household household) {
        householdRepository.delete(household);
    }

    private Household getHouseholdModel(Optional<Household> dadosHousehold){
        return dadosHousehold.orElseThrow(HouseholdNotFoundException::new);
    }
}
