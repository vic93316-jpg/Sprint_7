package all.models;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class Url {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static final String COURIER_PATH = "/api/v1/courier";
    public static final String LOGIN_PATH = "/api/v1/courier/login";
    public static final String ORDER_PATH = "/api/v1/orders";

    public static void init() {
        RestAssured.baseURI = BASE_URL;
    }
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
}
