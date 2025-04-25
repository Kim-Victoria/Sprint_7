import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import steps.OrderListSteps;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class GetListOfOrdersTest extends BaseTest {
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Тест на получение списка всех размещенных заказов")
    public void getOrderListTest() {
        Response response = OrderListSteps.getOrderList();
        assertThat("Статус-код должен быть 200", response.getStatusCode(), is(SC_OK));
        List<?> orders = response.jsonPath().getList("orders");
        assertThat("Список заказов не пустой", orders, is(not(empty())));
    }
}
