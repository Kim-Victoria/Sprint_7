package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static models.EndPoints.*;

public class OrderListSteps {

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
