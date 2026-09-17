import all.models.Url;
import creating.all.CreatingOrder;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;




import static org.hamcrest.CoreMatchers.notNullValue;


public class OrderListTest {

    private CreatingOrder creatingOrder ;

    @Before
    @Step("Подготовка: инициализация окружения для запроса списка заказов")
    public void setUp() {
        Url.init();
        creatingOrder = new CreatingOrder();
    }


    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    public void ordersListIsReturned() {
        creatingOrder.getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}