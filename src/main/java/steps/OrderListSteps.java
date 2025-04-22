package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.OrderModel;

import static io.restassured.RestAssured.given;

public class OrderListSteps {

    public static final String ORDER_LIST_PATH = "/api/v1/orders";

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_LIST_PATH)
                .then()
                .extract().response();
    }
}
