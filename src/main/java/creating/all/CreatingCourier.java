package creating.all;
import all.models.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreatingCourier {
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";


    // Создание курьера — принимает объект, возвращает Response
    @Step("Создать курьера с логином {courier.login}")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    // Логин курьера
    @Step("Авторизоваться под курьером {courier.login}")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(LOGIN_PATH);
    }

    // Удаление курьера по id
    @Step("Удалить курьера с id = {courierId}")
    public Response delete(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    // Утилита — получить id по логину/паролю
    @Step("Получить id курьера по логину {courier.login}")
    public int getCourierId(Courier courier) {
        Response response = login(courier);
        if (response.statusCode() == 200) {
            return response.path("id");
        }
        return -1;
    }
}
