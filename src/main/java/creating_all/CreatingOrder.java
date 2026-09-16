package creating_all;

import all_models.Order;
import all_models.Url;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static all_models.Url.ORDER_PATH;
import static io.restassured.RestAssured.given;

public class CreatingOrder {

    @Step("Создать заказ: имя = {order.firstName}, цвет = {order.color}")
    public Response create(Order order) {
        return given()
                .spec(Url.getRequestSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
    }
    // получение списка заказов
    @Step("Получить список заказов")
    public Response getOrders() {
        return given()
                .spec(Url.getRequestSpec())
                .when()
                .get(ORDER_PATH);
    }
}
