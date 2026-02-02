package utils;

import static io.restassured.RestAssured.*;

public class AuthUtil {

    public static String getAdminToken() {
        return given()
                .contentType("application/json")
                .body("""
                    {
                      "username": "admin",
                      "password": "admin123"
                    }
                    """)
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public static String getUserToken() {
        return given()
                .contentType("application/json")
                .body("""
                    {
                      "username": "testuser",
                      "password": "test123"
                    }
                    """)
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
