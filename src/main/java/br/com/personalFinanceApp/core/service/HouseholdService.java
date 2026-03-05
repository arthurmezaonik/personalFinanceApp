package br.com.personalFinanceApp.core.service;

import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import br.com.personalFinanceApp.core.dto.ResponseHouseholdDto;

import java.util.UUID;

public interface HouseholdService {

    ResponseHouseholdDto createHousehold(RequestHouseholdDto householdData);
    ResponseHouseholdDto getHouseholdById(UUID householdId);
    void deleteHouseholdById(UUID householdId);
    ResponseHouseholdDto updateHousehold(UUID householdId, RequestHouseholdDto householdData);
}
