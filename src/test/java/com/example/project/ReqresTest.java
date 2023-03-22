package com.example.project;

import api.data.ColorsData;
import api.data.UserData;
import api.helpers.Endpoints;
import api.registration.Registration;
import api.registration.RegistrationSuccess;
import api.registration.RegistrationUnsuccessful;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class Base {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}

public class ReqresTest extends Base {

    @Test
    @DisplayName("POJO: User avatar link contains the same id as the user")
    public void checkAvatarAndIdTest() {
        List<UserData> users = given()
                .when().log().all()
                .get(Endpoints.UserPage, 2)
                .then()
                .statusCode(200)
                .extract().body().jsonPath()
                .getList("data", UserData.class);

        assertAll(
            () -> users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString()))),
            () -> assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")))
        );
    }

    @Test
    @DisplayName("No POJO: User avatar link contains the same id as the user")
    public void checkAvatarAndIdTestNoPojo() {
        Response response = given()
                .when().log().all()
                .get(Endpoints.UserPage, 2)
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<String> emails = jsonPath.get("data.email");
        List<Integer> ids = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");

        for (int i = 0; i < avatars.size(); i++) {
            assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }
        assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("New user registered successfully")
    public void checkRegistrationSuccessTest() {
        int id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        RegistrationSuccess registrationSuccess = given()
                .body(user)
                .when().log().all()
                .post(Endpoints.UserRegisterPath)
                .then()
                .statusCode(200)
                .extract().as(RegistrationSuccess.class);

        assertAll(
            () -> assertNotNull(registrationSuccess.getId()),
            () ->assertNotNull(registrationSuccess.getToken()),
            () ->assertEquals(id, registrationSuccess.getId()),
            () ->assertEquals(token, registrationSuccess.getToken())
        );
    }

    @Test
    @DisplayName("User unsuccessful registration")
    public void checkUnsuccessfulRegistrationTest() {
        Registration user = new Registration("sydney@fife", "");
        RegistrationUnsuccessful registrationUnsuccessful = given()
                .body(user)
                .when().log().all()
                .post(Endpoints.UserRegisterPath)
                .then()
                .statusCode(400)
                .extract().as(RegistrationUnsuccessful.class);

        assertEquals("Missing email or username", registrationUnsuccessful.getError());
    }

    @Test
    @DisplayName("Get Sorted years test")
    public void getSortedYearsTest() {
        List<ColorsData> colors = given()
                .when().log().all()
                .get(Endpoints.ListResource)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        assertEquals(years, sortedYears);
    }

    @Test
    @DisplayName("Delete user with id: '2'")
    public void deleteUserTest() {

        given()
                .when().log().all()
                .delete(Endpoints.UserWithId, 2)
                .then()
                .statusCode(204);
    }
}
