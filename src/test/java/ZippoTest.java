import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {
        given()//hazirlik islemeri

                .when()//link ve aksiyon islemleri


                .then()
        //test ve extract islemleri
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()// log.all-->butun response gosterir
                .statusCode(200)
        ;
    }

    @Test
    public void contentTextType() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;

    }

    @Test
    public void logtest() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()

        ;

    }

    @Test
    public void checkStateInResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States"))
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void checkStateInResponseBodyHasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.state", hasItem("California"))//Butun state lerde aranan elaman var mi?
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest4() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills1"))
                .statusCode(200)
        ;

    }

    @Test
    public void bodyJsonPathTest5() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.'place name'", hasItem("Beverly Hills"))
                .statusCode(200)
        ;

    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1))//verilen pathdeki listin eleman sayisi
                .statusCode(200)
        ;

    }

    @Test
    public void bodyArrayHasSizeTest2() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .body("places.'place name'", hasSize(1))//verilen pathdeki listin eleman sayisi
                .statusCode(200)
        ;

    }

    @Test
    public void cobiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .body("places", hasSize(1))
                .body("places.'place name'", hasItem("Beverly Hills"))
                .body("places.state", hasItem("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("country", "us")
                .pathParam("zipcode", "90210")
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{country}/{zipcode}")
                .then()
                .log().body()
                .body("places", hasSize(1))

        ;

    }
    @Test
    public void pathParamTest2() {
        String country="us";
        String zipcode="90210";
        given()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode)
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{country}/{zipcode}")
                .then()
                .log().body()
                .body("places", hasSize(1))

        ;

    }
    @Test
    public void pathParamTest3() {
        String country="us";
        for (int i = 90210; i <90220 ; i++) {

        String zipcode=String.valueOf(i);
        given()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode)
                //.pathParam("zipcode", i)
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{country}/{zipcode}")
                .then()
                .log().body()
               .body("places", hasSize(1))

        ;

    }}

//https://gorest.co.in/public/v1/users?page=1
    @Test
    public void queryParamTest(){
        given()
                .param("page",1)//boyle yapinca param ile kendisi? mark verip parametre ekliyor
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
        .body("meta.pagination.page",equalTo(1))
        ;
    }

    @Test
    public void queryParamTest2(){
        for (int i = 1; i <10 ; i++) {
        given()
                .param("page",i)//boyle yapinca param ile kendisi? mark verip parametre ekliyor
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .body("meta.pagination.page",equalTo(i))
        ;
    }}





}
