import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.OrderModel;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.util.*;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static steps.OrderSteps.createOrder;
import static test.data.TestValues.*;

@RunWith(Parameterized.class)
    public class CreateOrderTest extends BaseTest {
    private final List<String> color;
    private int track;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Тест на успешное создание заказа с указанием всех обязательных полей")
    public void createOrderTest() {
        OrderModel order = new OrderModel(ORDER_FIRST_NAME, ORDER_LAST_NAME, ORDER_ADDRESS, ORDER_METRO_STATION, ORDER_PHONE, ORDER_RENT_TIME, ORDER_DELIVERY_DATE, "", color);
        Response response = createOrder(order);
        createOrderStep(response);
        track = response.then().extract().path("track");
    }

    @After
    @DisplayName("Удаление заказа")
    public void cancelOrder() {
        if (track != 0) {
            Response cancelOrder = OrderSteps.cancelOrder(track);
            checkDeleteOfOrderStep(cancelOrder);
        } else {
            System.out.println("Неверно указан номер заказа");
        }
    }
    @Step("Проверка успешного создания заказа")
    public void createOrderStep(Response response) {
        response.then().statusCode(SC_CREATED).body("track", notNullValue());
    }
    @Step ("Отмена заказа")
    public void checkDeleteOfOrderStep(Response response) {
        response.then().statusCode(SC_OK).body("ok", equalTo(true));
    }
}

