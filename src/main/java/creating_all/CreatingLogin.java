package creating_all;
import all_models.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreatingLogin {
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Авторизоваться под курьером с логином {courier.login}")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Получить id курьера по логину {courier.login}")
    public int getCourierId(Courier courier) {
        Response response = login(courier);
        return response.statusCode() == 200 ? response.path("id") : -1;
    }
}
