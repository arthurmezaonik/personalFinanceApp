package br.com.personalFinanceApp.core.mapper;

import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import br.com.personalFinanceApp.core.dto.ResponseHouseholdDto;
import br.com.personalFinanceApp.core.model.Household;
import org.springframework.stereotype.Component;

@Component
public class HouseholdMapper {

    public Household fromDtoToModel(RequestHouseholdDto householdData) {
        return new Household(
            null,
            householdData.name(),
            null
        );
    }

    public ResponseHouseholdDto fromModelToDto(Household household) {
        return new ResponseHouseholdDto(
            household.getId(),
            household.getName()
        );
    }
}
