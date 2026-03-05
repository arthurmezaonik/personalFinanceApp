package br.com.personalFinanceApp.core.controller.unit;

import br.com.personalFinanceApp.core.controller.ErrorHandler;
import br.com.personalFinanceApp.core.controller.HouseholdController;
import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import br.com.personalFinanceApp.core.dto.ResponseHouseholdDto;
import br.com.personalFinanceApp.core.exceptions.HouseholdNotFoundException;
import br.com.personalFinanceApp.core.service.HouseholdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static br.com.personalFinanceApp.utils.JsonToString.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HouseholdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HouseholdService householdService;

    @InjectMocks
    private HouseholdController householdController;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(householdController)
            .setControllerAdvice(new ErrorHandler())
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }, "/*")
            .build();
    }

    @Nested
    class createHousehold {
        @DisplayName("Should create a new household calling the correct function on the service.")
        @Test
        void createHouseholdSuccessfully() throws Exception {
            // Arrange
            RequestHouseholdDto requestHouseholdDto = new RequestHouseholdDto("House 1");
            ResponseHouseholdDto responseHouseholdDto = new ResponseHouseholdDto(UUID.randomUUID(), requestHouseholdDto.name());
            when(householdService.createHousehold(requestHouseholdDto)).thenReturn(responseHouseholdDto);

            // Act & Assert
            mockMvc.perform(
                post("/household")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(requestHouseholdDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseHouseholdDto.id().toString()))
                .andExpect(jsonPath("$.name").value(responseHouseholdDto.name()));
            verify(householdService, times(1)).createHousehold(requestHouseholdDto);
        }

        @DisplayName("Should throw a bad request if name is null.")
        @Test
        void createHouseholdFailedBadRequest() throws Exception {
            // Arrange
            RequestHouseholdDto requestHouseholdDto = new RequestHouseholdDto(null);

            // Act & Assert
            mockMvc.perform(
                    post("/household")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestHouseholdDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name field is required."));
            verify(householdService, times(0)).createHousehold(requestHouseholdDto);
        }
    }

    @Nested
    class getHouseholdById {
        @DisplayName("Should return the household by it's id.")
        @Test
        void getHouseholdByIdSuccessfully() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            ResponseHouseholdDto responseHouseholdDto = new ResponseHouseholdDto(id, "House 1");
            when(householdService.getHouseholdById(id)).thenReturn(responseHouseholdDto);

            // Act & Assert
            mockMvc.perform(
                get("/household/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseHouseholdDto.id().toString()))
                .andExpect(jsonPath("$.name").value(responseHouseholdDto.name()));
            verify(householdService, times(1)).getHouseholdById(id);
        }

        @DisplayName("Should return not found.")
        @Test
        void getHouseholdByIdFailedNotFound() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            when(householdService.getHouseholdById(any(UUID.class))).thenThrow(new HouseholdNotFoundException());

            // Act & Assert
            mockMvc.perform(
                    get("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Household not found."));
            verify(householdService, times(1)).getHouseholdById(id);

            }
    }

    @Nested
    class deleteHouseholdById {
        @DisplayName("Should delete the household by its id.")
        @Test
        void deleteHouseholdByIdSuccessfully() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            doNothing().when(householdService).deleteHouseholdById(id);

            // Act & Assert
            mockMvc.perform(
                    delete("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
            verify(householdService, times(1)).deleteHouseholdById(id);
        }

        @DisplayName("Should return not found when deleting a non-existent household.")
        @Test
        void deleteHouseholdByIdFailedNotFound() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            doThrow(new HouseholdNotFoundException()).when(householdService).deleteHouseholdById(any(UUID.class));

            // Act & Assert
            mockMvc.perform(
                    delete("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Household not found."));
            verify(householdService, times(1)).deleteHouseholdById(id);
        }
    }

    @Nested
    class updateHouseholdById {
        @DisplayName("Should update the household by its id.")
        @Test
        void updateHouseholdSuccessfully() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            RequestHouseholdDto requestDto = new RequestHouseholdDto("Updated House");
            ResponseHouseholdDto responseDto = new ResponseHouseholdDto(id, requestDto.name());
            when(householdService.updateHousehold(eq(id), any(RequestHouseholdDto.class))).thenReturn(responseDto);

            // Act & Assert
            mockMvc.perform(
                    patch("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
                .andExpect(jsonPath("$.name").value(responseDto.name()));
            verify(householdService, times(1)).updateHousehold(eq(id), any(RequestHouseholdDto.class));
        }

        @DisplayName("Should return not found when updating a non-existent household.")
        @Test
        void updateHouseholdFailedNotFound() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            RequestHouseholdDto requestDto = new RequestHouseholdDto("Updated House");
            when(householdService.updateHousehold(any(UUID.class), any(RequestHouseholdDto.class)))
                .thenThrow(new HouseholdNotFoundException());

            // Act & Assert
            mockMvc.perform(
                    patch("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Household not found."));
            verify(householdService, times(1)).updateHousehold(any(UUID.class), any(RequestHouseholdDto.class));
        }

        @DisplayName("Should return bad request when updating with an empty name.")
        @Test
        void updateHouseholdFailedBadRequest() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            RequestHouseholdDto requestDto = new RequestHouseholdDto("");

            // Act & Assert
            mockMvc.perform(
                    patch("/household/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name size needs to be between 1 and 255 characters."));
            verify(householdService, times(0)).updateHousehold(any(UUID.class), any(RequestHouseholdDto.class));
        }
    }
}
