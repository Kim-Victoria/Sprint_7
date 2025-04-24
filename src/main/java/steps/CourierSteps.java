package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CourierModel;
import static models.EndPoints.*;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    public static Response createCourier(CourierModel courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then()
                .extract().response();
    }
    public static Response loginCourier(CourierModel courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_PATH)
                .then()
                .extract().response();
    }
    public static Response deleteCourier(int id) {
        return given()
                .when()
                .delete(DELETE_COURIER_PATH + id)
                .then()
                .extract().response();
    }
    public static String generateUniqueLogin() {
        return "testCourier_" + System.currentTimeMillis();
    }
}
