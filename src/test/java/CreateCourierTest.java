import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;
import static test.data.TestValues.*;

public class CreateCourierTest extends BaseTest {
    private int courierId;
    private CourierModel courier;

    @After
    public void cleanUp() {
        if (courier != null) {
            try {
                courierId = loginCourier(courier)
                        .then()
                        .extract()
                        .path("id");
                if (courierId != 0) {
                    CourierSteps.deleteCourier(courierId);
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Тест на создание курьера со всеми валидными данными")
    public void createCourierSuccessful() {
        courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), COURIER_PASSWORD, COURIER_FIRSTNAME);
        Response response = createCourierStep(courier);
        checkSuccessfulCourierCreationStep(response);
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    @Description("Тест на проверку создания курьера с указанием уже существующего логина")
    public void createCourierWithSameLogin() {
        courier = generateCourierStep(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
        createCourier(courier);
        Response response = createCourierStep(courier);
        checkSameCourierLoginErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без обязательных полей")
    @Description("Тест на создание курьера со всеми пустыми полями в теле запроса")
    public void createCourierWithoutBody() {
        courier = generateCourierStep("", "", "");
        Response response = createCourierStep(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        courier = generateCourierStep("", COURIER_PASSWORD, COURIER_FIRSTNAME);
        Response response = createCourier(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Тест на создание курьера без указания пароля в теле запроса")
    public void createCourierWithoutPassword() {
        courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), "", COURIER_FIRSTNAME);
        Response response = createCourier(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Тест на создание курьера без указания имени в теле запроса")
    public void createCourierWithoutFirstName() {
        courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), COURIER_PASSWORD, "");
        Response response = createCourier(courier);
        checkSuccessfulCourierCreationStep(response);
    }

    @Step ("Создание модели курьера")
    public CourierModel generateCourierStep(String login, String password, String firstName) {
        return new CourierModel(login, password, firstName);
    }
    @Step ("Успешное создание курьера в системе")
    public Response createCourierStep(CourierModel courier) {
        return createCourier(courier);
    }
    @Step ("Проверка успешного создания курьера")
    public void checkSuccessfulCourierCreationStep(Response response) {
        response.then().statusCode(SC_CREATED).body("ok", equalTo(true));
    }
    @Step ("Проверка ошибки при попытке создания курьера с существующим логином")
    public void checkSameCourierLoginErrorStep(Response response) {
        response.then().statusCode(SC_CONFLICT).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Step ("Проверка создания курьера без обязательных полей")
    public void checkMissingFieldsErrorStep(Response response) {
        response.then().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

