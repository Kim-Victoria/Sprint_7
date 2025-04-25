import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static steps.CourierSteps.*;
import static test.data.TestValues.*;

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
    @Description("Тест на логин курьера в системе с указанием валидных данных в теле запроса")
    public void loginCourierTest() {
        Response loginResponse = loginCourier(courier);
        loginCourierStep(loginResponse);
        courierId = loginResponse.then().statusCode(SC_OK).extract().path("id");
    }

    @Test
    @DisplayName("Логин курьера без указания логина")
    @Description("Тест на логин курьера в системе без указания логина в теле запроса")
    public void loginCourierWithoutLogin() {
        CourierModel courierWithoutLogin = new CourierModel("", "1234");
        Response response = loginCourier(courierWithoutLogin);
        checkLoginCourierWithMissingFieldStep(response);
    }
    @Test
    @DisplayName("Логин курьера без указания пароля")
    @Description("Тест на логин курьера в системе без указания пароля в теле запроса")
    public void loginCourierWithoutPassword() {
        CourierModel courierWithoutPassword = new CourierModel(COURIER_LOGIN, "");
        Response response = loginCourier(courierWithoutPassword);
        checkLoginCourierWithMissingFieldStep(response);
    }
    @Test
    @DisplayName("Логин курьера с несуществующим логином")
    @Description("Тест на логин курьера в системе с указанием несуществующего логина в теле запроса")
    public void loginCourierWithUnknownLogin() {
        CourierModel courierWithUnknownLogin = new CourierModel("unregistered_user", "1234");
        Response response = loginCourier(courierWithUnknownLogin);
        checkLoginWithWrongDataStep(response);
    }
    @Test
    @DisplayName("Логин курьера с неправильным паролем")
    @Description("Тест на логин курьера в системе с указанием неправильного пароля в теле запроса")
    public void loginCourierWithWrongPassword() {
        CourierModel courierWithWrongPassword = new CourierModel(COURIER_LOGIN, "wrong_password");
        Response response = loginCourier(courierWithWrongPassword);
        checkLoginWithWrongDataStep(response);
    }
    @Step ("Успешный логин курьера в системе")
    public void loginCourierStep(Response response) {
        response.then().statusCode(SC_OK).body("id", notNullValue());
    }
    @Step ("Проверка логина курьера без указания обязательных полей")
    public void checkLoginCourierWithMissingFieldStep(Response response) {
        response.then().statusCode(SC_BAD_REQUEST).body("message", containsString("Недостаточно данных для входа"));
    }
    @Step ("Проверка логина курьера с неправильными данными")
    public void checkLoginWithWrongDataStep(Response response) {
        response.then().statusCode(SC_NOT_FOUND).body("message", containsString("Учетная запись не найдена"));
    }

}
