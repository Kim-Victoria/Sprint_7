package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.OrderModel;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static models.EndPoints.*;

public class OrderSteps {

    public static Response createOrder(OrderModel order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .extract().response();
    }
    public static Response cancelOrder(int track) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("track", track);

        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .put(CANCEL_ORDER_PATH +"?track=" + track);
    }
}

