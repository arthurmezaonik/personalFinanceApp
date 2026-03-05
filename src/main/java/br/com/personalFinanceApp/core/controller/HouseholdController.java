package br.com.personalFinanceApp.core.controller;

import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import br.com.personalFinanceApp.core.dto.ResponseHouseholdDto;
import br.com.personalFinanceApp.core.service.HouseholdService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/household")
@Slf4j
public class HouseholdController {

    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @PostMapping
    public ResponseEntity<ResponseHouseholdDto> createHousehold(@Valid @RequestBody RequestHouseholdDto householdData){
        log.info("createHousehold(): household data {}", householdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(householdService.createHousehold(householdData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHouseholdDto> getHouseholdById(@PathVariable UUID id){
        log.info("getHouseholdById(): household id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(householdService.getHouseholdById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouseholdById(@PathVariable UUID id){
        log.info("deleteHouseholdById(): household id {}", id);
        householdService.deleteHouseholdById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseHouseholdDto> updateHouseholdById(@PathVariable UUID id, @Valid @RequestBody RequestHouseholdDto householdData){
        log.info("updateHouseholdById(): household id {}, new household data {}", id,  householdData);
        return ResponseEntity.status(HttpStatus.OK).body(householdService.updateHousehold(id, householdData));
    }
}
