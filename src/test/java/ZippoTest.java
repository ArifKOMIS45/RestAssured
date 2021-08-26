import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.util.Asserts;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

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
                .body("places[0].'place name'", equalTo("Beverly Hills"))
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
        String country = "us";
        String zipcode = "90210";
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
        String country = "us";
        for (int i = 90210; i < 90220; i++) {

            String zipcode = String.valueOf(i);
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

        }
    }

    //https://gorest.co.in/public/v1/users?page=1
    @Test
    public void queryParamTest() {
        given()
                .param("page", 1)//boyle yapinca param ile kendisi? mark verip parametre ekliyor
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .body("meta.pagination.page", equalTo(1))
        ;
    }

    @Test
    public void queryParamTest2() {
        for (int i = 1; i < 10; i++) {
            given()
                    .param("page", i)//boyle yapinca param ile kendisi? mark verip parametre ekliyor
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

    @BeforeClass
    public void setup() {
        baseURI = "http://api.zippopotam.us";// Restassure kendi static degiskeni tanimli deger atiyor
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void bodyArrayHasSizeTest_baseUriTest_responseSpecification() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")//url nin basinda http yoksa baseUri deki deger otamatik geliyor
                .then()
                .body("places", hasSize(1))//verilen pathdeki listin eleman sayisi
                .spec(responseSpecification)
        ;
    }

    //jspm extract
    @Test
    public void extractingJsonPaty() {
        String place_name = given()//.spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                //.spec(responseSpecification)
                .extract().path("places[0].'place name'")// extract metodu ie gigen ile basliyan satir
                // disariya bir deger dondurur hale geldi  .. ensonunda extract kullanilmali
                ;
        System.out.println("place name= " + place_name);

    }


    @Test
    public void extractingJsonPathInt() {
        int limit =
                given()
                        .param("page", 1)
                        //.log().uri()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPathIntList() {
        List<Integer> idler =  //data.id demek tum idler demektir
                given()
                        .param("page", 1)
                        //.log().uri()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .extract().path("data.id");
        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPathStringList() {
        List<String> koyler = given()
                //.spec(requestSpecification)
                .when()
                .get("/tr/01000")
                .then()
                //.spec(responseSpecification)
                .extract().path("places.'place name'")
                ;
        System.out.println("koyler = " + koyler);
        Assert.assertTrue(koyler.contains("Büyükdikili Köyü"));

    }


@Test
    public void extractingJsonPOJO()    // json object
    {Location location=
    given()
            .when()
            .get("/us/90210")
                        
            .then()
    .extract().as(Location.class)
    ;
    System.out.println("location = " + location);
    System.out.println("location.getCountry() = " + location.getCountry());
    System.out.println("location = " + location.getPlaces());
    System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());
    
}













}
