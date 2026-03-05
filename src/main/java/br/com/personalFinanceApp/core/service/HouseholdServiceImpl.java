package br.com.personalFinanceApp.core.service;

import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import br.com.personalFinanceApp.core.dto.ResponseHouseholdDto;
import br.com.personalFinanceApp.core.mapper.HouseholdMapper;
import br.com.personalFinanceApp.core.model.Household;
import br.com.personalFinanceApp.core.repository.HouseholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HouseholdServiceImpl implements HouseholdService {

    private final HouseholdRepository householdRepository;
    private final HouseholdMapper householdMapper;

    public HouseholdServiceImpl(HouseholdRepository householdRepository, HouseholdMapper householdMapper) {
        this.householdRepository = householdRepository;
        this.householdMapper = householdMapper;
    }

    @Override
    @Transactional
    public ResponseHouseholdDto createHousehold(RequestHouseholdDto householdData) {
        // converter para model e salvar
        Household household = householdRepository.save(householdMapper.fromDtoToModel(householdData));

        // converter para dto e retornar
        return householdMapper.fromModelToDto(household);
    }

    @Override
    public ResponseHouseholdDto getHouseholdById(UUID householdId) {
        return householdMapper.fromModelToDto(householdRepository.findById(householdId));
    }

    @Override
    @Transactional
    public void deleteHouseholdById(UUID householdId) {
        Household household = householdRepository.findById(householdId);
        householdRepository.delete(household);
    }

    @Override
    @Transactional
    public ResponseHouseholdDto updateHousehold(UUID householdId, RequestHouseholdDto householdData) {
        Household household = householdRepository.findById(householdId);

        if (!household.getName().equals(householdData.name())) {
            household.setName(householdData.name());
            householdRepository.save(household);
        }

        return householdMapper.fromModelToDto(household);
    }
}
