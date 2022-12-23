package com.example.project;

import api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ReqresTest {

    @Test
    @DisplayName("User avatar link contains the same id as the user")
    public void checkAvatarAndIdTest() {
        Specification.installSpecification(
                Specification.requestSpecification(Endpoints.URL),
                Specification.responseSpecificationOK200()
        );

        List<UserData> users = given()
                .when().get(Endpoints.UserPath)
                .then().log().all()
                .extract().body().jsonPath()
                .getList("data", UserData.class);

        users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString())));
        assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("reqres.in")));
    }

    @Test
    @DisplayName("New user registered successfully")
    public void checkRegistrationSuccessTest() {
        Specification.installSpecification(
                Specification.requestSpecification(Endpoints.URL),
                Specification.responseSpecificationOK200()
        );

        int id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        RegistrationSuccess registrationSuccess = given()
                .body(user)
                .when().post(Endpoints.UserRegisterPath)
                .then().log().all()
                .extract().as(RegistrationSuccess.class);

        assertNotNull(registrationSuccess.getId());
        assertNotNull(registrationSuccess.getToken());
        assertEquals(id, registrationSuccess.getId());
        assertEquals(token, registrationSuccess.getToken());
    }

    @Test
    @DisplayName("User unsuccessful registration")
    public void checkUnsuccessfulRegistrationTest() {
        Specification.installSpecification(
                Specification.requestSpecification(Endpoints.URL),
                Specification.responseSpecificationError400()
        );

        Registration user = new Registration("sydney@fife", "");
        RegistrationUnsuccessful registrationUnsuccessful = given()
                .body(user)
                .when().post(Endpoints.UserRegisterPath)
                .then().log().all()
                .extract().as(RegistrationUnsuccessful.class);

        assertEquals("Missing password", registrationUnsuccessful.getError());
    }

    @Test
    @DisplayName("Get Sorted years test")
    public void getSortedYearsTest() {
        Specification.installSpecification(
                Specification.requestSpecification(Endpoints.URL),
                Specification.responseSpecificationOK200()
        );

        List<ColorsData> colors = given()
                .when().get(Endpoints.ListResource)
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        assertEquals(years, sortedYears);
    }

    @Test
    @DisplayName("Delete user with id: '2'")
    public void deleteUserTest() {
        Specification.installSpecification(
                Specification.requestSpecification(Endpoints.URL),
                Specification.responseSpecificationUniqueStatus(204)
        );
        given()
                .when().delete(Endpoints.UserWithId2)
                .then().log().all();
    }
}










