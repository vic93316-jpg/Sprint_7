import all.models.Url;
import creating.all.CreatingOrder;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import all.models.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)

public class CreatingOrderTest {
    private CreatingOrder creatingOrder;
    private final List<String> color;


    public CreatingOrderTest(List<String> color) {
        this.color = color;
    }


    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        });
    }


    @Before
    @Step("Подготовка: инициализация окружения для создания заказа")
    public void setUp() {
        Url.init();
        creatingOrder = new CreatingOrder();
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    public void orderCanBeCreatedWithDifferentColors() {

        Order order = new Order(
                "Naruto",
                "Uzumaki",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color
        );

        Response response = creatingOrder.create(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
