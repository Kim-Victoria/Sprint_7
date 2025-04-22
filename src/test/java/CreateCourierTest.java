import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Test;
import steps.CourierSteps;

import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;
import static testData.TestValues.*;

public class CreateCourierTest extends BaseTest {
    private int courierId;

    @After
    public void cleanUp() {
        if (courierId != 0) {
            CourierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccessful() {
        CourierModel courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), COURIER_PASSWORD, COURIER_FIRSTNAME);
        Response response = createCourierStep(courier);
        checkSuccessfulCourierCreationStep(response);
        courierId = loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void createCourierWithSameLogin() {
        CourierModel courier = generateCourierStep(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
        createCourier(courier);
        Response response = createCourierStep(courier);
        checkSameCourierLoginErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без обязательных полей")
    public void createCourierWithoutBody() {
        CourierModel courier = generateCourierStep("", "", "");
        Response response = createCourierStep(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        CourierModel courier = generateCourierStep("", COURIER_PASSWORD, COURIER_FIRSTNAME);
        Response response = createCourier(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        CourierModel courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), "", COURIER_FIRSTNAME);
        Response response = createCourier(courier);
        checkMissingFieldsErrorStep(response);
    }
    @Test
    @DisplayName("Создание курьера без имени")
    public void createCourierWithoutFirstName() {
        CourierModel courier = generateCourierStep(COURIER_LOGIN + System.currentTimeMillis(), COURIER_PASSWORD, "");
        Response response = createCourier(courier);
        checkSuccessfulCourierCreationStep(response);
        courierId = loginCourier(courier)
                .then()
                .extract()
                .path("id");
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
        response.then().statusCode(201).body("ok", equalTo(true));
    }
    @Step ("Проверка ошибки при попытке создания курьера с существующим логином")
    public void checkSameCourierLoginErrorStep(Response response) {
        response.then().statusCode(409).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Step ("Проверка создания курьера без обязательных полей")
    public void checkMissingFieldsErrorStep(Response response) {
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

