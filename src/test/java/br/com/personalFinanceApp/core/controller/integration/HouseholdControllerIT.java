package br.com.personalFinanceApp.core.controller.integration;

import br.com.personalFinanceApp.core.dto.RequestHouseholdDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class HouseholdControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Nested
    class createHousehold {
        @DisplayName("Should create a new household successfully.")
        @Test
        void createHouseholdSuccessfully(){
            // Arrange
            RequestHouseholdDto requestHouseholdDto = new RequestHouseholdDto("House 1");

            // Act & Assert
            given()
                .contentType(ContentType.JSON)
                .body(requestHouseholdDto)
            .when()
                .post("/household")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestHouseholdDto.name()));
        }

        @DisplayName("Should return 400 Bad Request if name is null.")
        @Test
        void createHouseholdFailedBadRequest(){
            // Arrange
            RequestHouseholdDto requestHouseholdDto = new RequestHouseholdDto(null);

            // Act & Assert
            given()
                .contentType(ContentType.JSON)
                .body(requestHouseholdDto)
            .when()
                .post("/household")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name field is required."));
        }
    }

    @Nested
    class getHouseholdById {
        @DisplayName("Should return the household by its id.")
        @Test
        void getHouseholdByIdSuccessfully() {
            String householdId = "550e8400-e29b-41d4-a716-446655440000";

            given()
            .when()
                .get("/household/{id}", householdId)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(householdId))
                .body("name", equalTo("Test Household"));
        }

        @DisplayName("Should return not found for non-existent id.")
        @Test
        void getHouseholdByIdFailedNotFound() {
            String nonExistentId = "00000000-0000-0000-0000-000000000000";

            given()
            .when()
                .get("/household/{id}", nonExistentId)
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class deleteHouseholdById {
        @DisplayName("Should delete the household by its id.")
        @Test
        void deleteHouseholdByIdSuccessfully() {
            String householdId = "550e8400-e29b-41d4-a716-446655440010";

            given()
            .when()
                .delete("/household/{id}", householdId)
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        }
    }

    @Nested
    class updateHouseholdById {
        @DisplayName("Should update the household by its id.")
        @Test
        void updateHouseholdSuccessfully() {
            String householdId = "550e8400-e29b-41d4-a716-446655440000";
            RequestHouseholdDto updateDto = new RequestHouseholdDto("Updated House");

            given()
                .contentType(ContentType.JSON)
                .body(updateDto)
            .when()
                .patch("/household/{id}", householdId)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(updateDto.name()));
        }

        @DisplayName("Should return bad request when updating with null name.")
        @Test
        void updateHouseholdFailedBadRequest() {
            String householdId = "550e8400-e29b-41d4-a716-446655440000";
            RequestHouseholdDto updateDto = new RequestHouseholdDto(null);

            given()
                .contentType(ContentType.JSON)
                .body(updateDto)
            .when()
                .patch("/household/{id}", householdId)
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name field is required."));
        }
    }
}
