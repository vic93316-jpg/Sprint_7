import all_models.Courier;
import creating_all.CreatingCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static all_models.Url.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreatingCourierTest {
    private CreatingCourier creatingCourier;
    private Courier courier;
    private int courierId = -1;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        creatingCourier = new CreatingCourier();

        String login = "courier_" + System.currentTimeMillis();
        courier = new Courier(login, "1234", "Saske");
    }

    @After
    public void tearDown() {
        if (courierId != -1) {
            creatingCourier.delete(courierId);
        }
    }

    // 1. Курьера можно создать
    @Test
    @DisplayName("Курьера можно создать")
    @Description("Успешный запрос возвращает 201 и ok: true")
    public void courierCanBeCreated() {
        Response response = creatingCourier.create(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = creatingCourier.getCourierId(courier);
    }

    // 2. Нельзя создать двух одинаковых курьеров
    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoIdenticalCouriers() {
        creatingCourier.create(courier).then().statusCode(201);
        courierId = creatingCourier.getCourierId(courier);

        creatingCourier.create(courier).then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    // 3. Все обязательные поля — успех
    @Test
    @DisplayName("Курьера можно создать со всеми обязательными полями")
    public void courierCanBeCreatedWithAllRequiredFields() {
        creatingCourier.create(courier).then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = creatingCourier.getCourierId(courier);
    }

    // 4. Правильный код ответа
    @Test
    @DisplayName("Успешный запрос возвращает код 201")
    public void successfulRequestReturns201() {
        creatingCourier.create(courier).then().statusCode(201);
        courierId = creatingCourier.getCourierId(courier);
    }

    // 5. ok: true
    @Test
    @DisplayName("Успешный запрос возвращает ok: true")
    public void successfulRequestReturnsOkTrue() {
        creatingCourier.create(courier).then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = creatingCourier.getCourierId(courier);
    }

    // 6a. Нет логина
    @Test
    @DisplayName("Если нет логина — возвращается ошибка 400")
    public void errorWhenLoginIsMissing() {
        Courier invalid = new Courier(null, "1234", "Saske");

        creatingCourier.create(invalid).then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // 6b. Нет пароля
    @Test
    @DisplayName("Если нет пароля — возвращается ошибка 400")
    public void errorWhenPasswordIsMissing() {
        Courier invalid = new Courier(courier.getLogin(), null, "Saske");

        creatingCourier.create(invalid).then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // 7. Логин уже занят
    @Test
    @DisplayName("Если логин уже занят — возвращается ошибка 409")
    public void errorWhenLoginAlreadyExists() {
        creatingCourier.create(courier).then().statusCode(201);
        courierId = creatingCourier.getCourierId(courier);

        Courier duplicate = new Courier(courier.getLogin(), "other", "Other");
        creatingCourier.create(duplicate).then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}

