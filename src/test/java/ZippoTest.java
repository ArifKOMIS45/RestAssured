import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

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



}
