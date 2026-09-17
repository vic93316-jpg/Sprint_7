import all.models.Courier;
import creating.all.CreatingCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import all.models.Url;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreatingLoginTest {
    private CreatingCourier creatingCourier;
    private Courier courier;
    private int courierId = -1;

    @Before
    @Step("Подготовка: создать курьера с уникальным логином")
    public void setUp() {
        Url.init();
        creatingCourier = new CreatingCourier();

        courier = new Courier(
                "courier_" + System.currentTimeMillis(),
                "1234",
                "Saske"
        );

        // Создаём курьера — иначе логиниться будет не под кем
        creatingCourier.create(courier).then().statusCode(201);
        courierId = creatingCourier.getCourierId(courier);
    }


    @After
    @Step("Очистка: удалить созданного курьера")
    public void tearDown() {
        if (courierId != -1) {
            creatingCourier.delete(courierId);
        }
    }

    // 1. Курьер может авторизоваться
    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Успешный логин возвращает 200 и id")
    public void courierCanLogin() {
        Response response = creatingCourier.login(courier);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    // 2. Для авторизации нужно передать все обязательные поля
    @Test
    @DisplayName("Для авторизации нужны все обязательные поля")
    public void loginRequiresAllRequiredFields() {
        Courier valid = new Courier(courier.getLogin(), courier.getPassword(), null);

        creatingCourier.login(valid).then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    // 3a. Неправильный логин → ошибка
    @Test
    @DisplayName("Ошибка при неверном логине")
    public void errorWhenLoginIsIncorrect() {
        Courier wrong = new Courier("wrong_login_xyz", courier.getPassword(), null);

        creatingCourier.login(wrong).then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // 3b. Неправильный пароль → ошибка
    @Test
    @DisplayName("Ошибка при неверном пароле")
    public void errorWhenPasswordIsIncorrect() {
        Courier wrong = new Courier(courier.getLogin(), "wrong_password", null);

        creatingCourier.login(wrong).then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // 4a. Нет логина → ошибка
    @Test
    @DisplayName("Ошибка, если не передан логин")
    public void errorWhenLoginIsMissing() {
        Courier withoutLogin = new Courier(null, courier.getPassword(), null);

        creatingCourier.login(withoutLogin).then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // 4b. Нет пароля → ошибка
    @Test
    @DisplayName("Ошибка, если не передан пароль")
    public void errorWhenPasswordIsMissing() {
        Courier withoutPassword = new Courier(courier.getLogin(), null, null);

        creatingCourier.login(withoutPassword).then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // 5. Несуществующий пользователь → ошибка
    @Test
    @DisplayName("Ошибка при авторизации несуществующего пользователя")
    public void errorWhenUserDoesNotExist() {
        Courier ghost = new Courier(
                "ghost_" + System.currentTimeMillis(),
                "1234",
                null
        );

        creatingCourier.login(ghost).then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // 6. Успешный запрос возвращает id
    @Test
    @DisplayName("Успешный логин возвращает id")
    public void successfulLoginReturnsId() {
        creatingCourier.login(courier).then()
                .statusCode(200)
                .body("id", notNullValue());
    }
}

