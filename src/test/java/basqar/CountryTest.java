package basqar;

import basqar.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CountryTest {
    Cookies cookies;
    String id;
    String randomGenName = RandomStringUtils.randomAlphabetic(6);
    String randomGenCode = RandomStringUtils.randomAlphabetic(3);

    @BeforeClass
    public void loginBasqar() {
        baseURI = "https://demo.mersys.io";
        Map<String, String> credential = new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        //.log().body()
                        .extract().response().getDetailedCookies();
        System.out.println("cookies = " + cookies);
    }

    @Test
    public void createCountry() {

        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);
        id =
                given()
                        .cookies(cookies)//gelen bilgiyi(token bilgileri) gondermis olduk
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("/school-service/api/countries")
                        .then()
                        .log().body()
                        .body("name", equalTo(randomGenName))
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
        System.out.println("ID = " + id);
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {
        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        given()
                .cookies(cookies)//gelen bilgiyi(token bilgileri) gondermis olduk
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .body("message", equalTo("The Country with Name \"" + randomGenName + "\" already exists."))
                .statusCode(400)
        ;
    }


    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {
        Country country = new Country();
        country.setName(RandomStringUtils.randomAlphabetic(12));
        country.setCode(RandomStringUtils.randomAlphabetic(5));
        country.setId(id);
        given()
                .cookies(cookies)//gelen bilgiyi(token bilgileri) gondermis olduk
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .put("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(country.getName()));
    }
    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

        given()
                .cookies(cookies)//gelen bilgiyi(token bilgileri) gondermis olduk
                .pathParam("id",id)
                .when()
                .delete("/school-service/api/countries/{id}")
                .then()
                .log().body()
                .statusCode(200)
                ;
    }
    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

        given()
                .cookies(cookies)//gelen bilgiyi(token bilgileri) gondermis olduk
                .pathParam("id",id)
                .when()
                .delete("/school-service/api/countries/{id}")
                .then()
                .log().body()
                .body("message",equalTo("Country not found"))
                .statusCode(404)
        ;
    }

}
