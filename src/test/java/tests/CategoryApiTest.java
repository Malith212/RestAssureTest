package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import utils.AuthUtil;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CategoryApiTest extends BaseTest {

    static int categoryId;
    static int subCategoryId;

    // TC_CAT_ADM_06 - Create main category
    @Test
    public void createMainCategory() {
        categoryId =
                given()
                        .header("Authorization", "Bearer " + AuthUtil.getAdminToken())
                        .contentType("application/json")
                        .body("""
                    {
                      "name": "Electro",
                      "parentId": null
                    }
                    """)
                        .when()
                        .post("/api/categories")
                        .then()
                        .statusCode(201)
                        .body("name", equalTo("Electro"))
                        .extract()
                        .path("id");
    }

    // TC_CAT_ADM_07 - Create sub-category
    @Test(dependsOnMethods = "createMainCategory")
    public void createSubCategory() {
        subCategoryId =
                given()
                        .header("Authorization", "Bearer " + AuthUtil.getAdminToken())
                        .contentType("application/json")
                        .body("""
                    {
                      "name": "Mobile",
                      "parentId": %d
                    }
                    """.formatted(categoryId))
                        .when()
                        .post("/api/categories")
                        .then()
                        .statusCode(201)
                        .body("name", equalTo("Mobile"))
                        .extract()
                        .path("id");
    }

    // TC_CAT_ADM_08 - Update category
    @Test(dependsOnMethods = "createMainCategory")
    public void updateCategory() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getAdminToken())
                .contentType("application/json")
                .body("""
            {
              "name": "Electro1"
            }
            """)
                .when()
                .put("/api/categories/" + categoryId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Electro1"));
    }

    // TC_CAT_ADM_09 - Delete category
    @Test(dependsOnMethods = "updateCategory")
    public void deleteCategory() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getAdminToken())
                .when()
                .delete("/api/categories/" + categoryId)
                .then()
                .statusCode(204);
    }

    // TC_CAT_ADM_10 - Get paginated categories
    @Test
    public void getPaginatedCategories() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getAdminToken())
                .when()
                .get("/api/categories?page=0&size=10&sortField=id&sortDir=asc")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    // TC_CAT_USR_06 - Get all categories (User)
    @Test
    public void getCategoriesAsUser() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getUserToken())
                .when()
                .get("/api/categories")
                .then()
                .statusCode(200);
    }

    // TC_CAT_USR_07 - Get category by ID (User)
    @Test(dependsOnMethods = "createMainCategory")
    public void getCategoryByIdAsUser() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getUserToken())
                .when()
                .get("/api/categories/" + categoryId)
                .then()
                .statusCode(200)
                .body("id", equalTo(categoryId));
    }

    // TC_SLS_USR_11 - Create category as user (Forbidden)
    @Test
    public void createCategoryAsUserForbidden() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getUserToken())
                .contentType("application/json")
                .body("""
            {
              "name": "BadCat"
            }
            """)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(403);
    }

    // TC_SLS_USR_12 - Update category as user (Forbidden)
    @Test(dependsOnMethods = "createMainCategory")
    public void updateCategoryAsUserForbidden() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getUserToken())
                .contentType("application/json")
                .body("""
            {
              "name": "Hack"
            }
            """)
                .when()
                .put("/api/categories/" + categoryId)
                .then()
                .statusCode(403);
    }

    // TC_SLS_USR_13 - Delete category as user (Forbidden)
    @Test(dependsOnMethods = "createMainCategory")
    public void deleteCategoryAsUserForbidden() {
        given()
                .header("Authorization", "Bearer " + AuthUtil.getUserToken())
                .when()
                .delete("/api/categories/" + categoryId)
                .then()
                .statusCode(403);
    }

    //tested branch created successfully
}
