import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.hamcrest.CoreMatchers.*;
import static steps.CourierSteps.*;
import static testData.TestValues.*;

public class LoginCourierTest extends BaseTest{
    private int courierId;
    private CourierModel courier;

    @Before
    public void setUp() {
        courier = new CourierModel(generateUniqueLogin(), COURIER_PASSWORD, COURIER_FIRSTNAME);
        createCourier(courier);
    }
    @After
        public void tearDown() {
        if (courierId != 0) {
            CourierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешный логин курьера в системе")
    public void loginCourierTest() {
        Response loginResponse = loginCourier(courier);
        loginCourierStep(loginResponse);
        courierId = loginResponse.then().statusCode(200).extract().path("id");
    }

    @Test
    @DisplayName("Логин курьера без указания логина")
    public void loginCourierWithoutLogin() {
        CourierModel courierWithoutLogin = new CourierModel("", "1234");
        Response response = loginCourier(courierWithoutLogin);
        checkLoginCourierWithoutLoginStep(response);
    }
    @Test
    @DisplayName("Логин курьера без указания пароля")
    public void loginCourierWithoutPassword() {
        CourierModel courierWithoutPassword = new CourierModel(COURIER_LOGIN, "");
        Response response = loginCourier(courierWithoutPassword);
        checkLoginCourierWithoutPasswordStep(response);
    }
    @Test
    @DisplayName("Логин курьера с несуществующим логином")
    public void loginCourierWithUnknownLogin() {
        CourierModel courierWithUnknownLogin = new CourierModel("unregistered_user", "1234");
        Response response = loginCourier(courierWithUnknownLogin);
        checkLoginCourierWithUnknownLoginStep(response);
    }
    @Test
    @DisplayName("Логин курьера с неправильным паролем")
    public void loginCourierWithWrongPassword() {
        CourierModel courierWithWrongPassword = new CourierModel(COURIER_LOGIN, "wrong_password");
        Response response = loginCourier(courierWithWrongPassword);
        checkLoginWithWrongPasswordStep(response);
    }
    @Step ("Успешный логин курьера в системе")
    public void loginCourierStep(Response response) {
        response.then().statusCode(200).body("id", notNullValue());
    }
    @Step ("Проверка логина курьера без указания логина")
    public void checkLoginCourierWithoutLoginStep(Response response) {
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }
    @Step ("Проверка логина курьера без указания пароля")
    public void checkLoginCourierWithoutPasswordStep(Response response) {
        response.then().statusCode(400).body("message", containsString("Недостаточно данных для входа"));
    }
    @Step ("Проверка логина с несуществующим логином")
    public void checkLoginCourierWithUnknownLoginStep(Response response) {
        response.then().statusCode(404).body("message", containsString("Учетная запись не найдена"));
    }
    @Step ("Проверка логина курьера с неправильным паролем")
    public void checkLoginWithWrongPasswordStep(Response response) {
        response.then().statusCode(404).body("message", containsString("Учетная запись не найдена"));
    }

}
