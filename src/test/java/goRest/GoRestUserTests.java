package goRest;

import goRest.Model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUserTests {
    int userID = 0;

    @Test(priority = 1)
    public void getUsers() {
        List<User> userList =
                given()
                        .when().
                        get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        //.log().body()
                        .extract().jsonPath().getList("data", User.class);

        for (User user : userList) {
            System.out.println("user = " + user);
        }
    }

// Create User için POSTMAN de yapılanlar

//    JSON olarak gidecek body  {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
//
//    header ın içine
//    Authorization  Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f
//
//    POST ile https://gorest.co.in/public/v1/users çağırdık
//    id yi okuduk ve global bir değişkene attık ki, diğer reqest larde kullanabilelim

    @Test(priority = 2)
    public void createUsers() {
        userID = given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"ismet\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")

                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(201)
                .log().body()
                .extract().jsonPath().getInt("data.id");
        System.out.println("userID = " + userID);

    }

    public String getRandomEmail() {// yukardaki post icin random email yapmak icin kullandk
        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString + "@gmail.com";
    }

    @Test(priority = 3)
    public void getUserById() {
        //createUsers();
        given()
                .pathParam("userID", userID)
                .when()
                .get("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .statusCode(200)
                .body("data.id", equalTo(userID));

    }

    @Test(priority = 4)
    public void updateUserById() {
        String isim = "Arif2 KOMIS";
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("userID", userID)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + isim + "\"}")

                .when()
                .put("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .statusCode(200)
                .body("data.name", equalTo(isim))
                .log().body()
        ;

    }

    @Test(priority = 5)
    public void deleteUserbyId() {
        createUsers();
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("userID", userID)
                .log().uri()

                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")
//pathparam olursa:    uri= https://gorest.co.in/public/v1/users/1890
//param olursa:        uri=https://gorest.co.in/public/v1/users/%7BuserID%7D?userID=1891
                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(priority = 6)
    public void deleteUserbyIdNegative() {
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("userID", userID)

                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .statusCode(404)
        ;
    }

    /*
Extract, patsh, as, jsonPath
```istediğim veriyi istediğim tipte nasıl alırım? ama bu extract işleminde amaç bu.
{
    "meta": {  meta
        "pagination": {   pagination
            "total": 1633,  -> .extract.path("meta.pagination.total")   -> int total , ototatik int dönüşümü kendisi de int olduğu için.
                            -> .extract.jsonPath.getInt("meta.pagination.total")  -> int e dönüştürüp öyle int e eşitliyor.
            "pages": 82,
            "page": 1,
            "limit": 20,
            "links": {     link
                "previous": null,
                "current": "https://gorest.co.in/public/v1/users?page=1",
                "next": "https://gorest.co.in/public/v1/users?page=2"
            }
        }
    },
    "data": [ User
        {
            "id": 1685,
            "name": "Qmzn",        -> bütün nameleri almak için    extract.path("data.name") -> List<String> değişkene atarken çevirecek
                                   -> extract.jsonPath().getList("data.name") -> list e çevirip öyle vermek olacaktı
            "email": "qvjsulha@gmail.com",
            "gender": "male",
            "status": "active"
        },
        {
            "id": 1687,
            "name": "Mavie Test 3",
            "email": "test3@email.com",
            "gender": "female",
            "status": "active"
        }
   ]
}


-> Bütün veriyi bir toplu olarak için -> 4 tane class yazman lazım ve -> extract.as(Genel.class)

public class Genel
{
     Meta meta;
     List<User> data;
}

genel.getMeta().getPagination().getTotal    -> bütün verilere bu şekilde ulaşabiliyorum.
-------------------------------------------------------------------------------------------------

jsonPath esas nerede devreye giriyor, yukarıdakilerde neyi yapamıyoruz ki jsonPath e ihtiyacımız oluyor ?


1- Sadece Linkleri bir Class tipinde elde etmek istiyorum ?

         extract.jsonPath.getObject("meta.pagination.links", Link.class)

         bu sorunu jsonPath çözebiliyor.Diğer classları yazmama gerek olmadan


2- tip dönüşümünü veriyi alırken yapması da bir avantaj.*/


    @Test
    public void responseSample() {
        Response donenSonuc =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().response();

        List<User> userList = donenSonuc.jsonPath().getList("data", User.class);
        int total = donenSonuc.jsonPath().getInt("meta.pagination.total");
        int limit = donenSonuc.jsonPath().getInt("meta.pagination.limit");
        User firstUser = donenSonuc.jsonPath().getObject("data[0]", User.class);

        System.out.println("userList = " + userList);
        System.out.println("total = " + total);
        System.out.println("limit = " + limit);
        System.out.println("firstUser = " + firstUser);

    }

    @Test
    public void createUsers2() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", "Arif");
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");

        userID = given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .body(newUser)

                .when()
                .post("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(201)
                .log().body()
                .extract().jsonPath().getInt("data.id");

        System.out.println("userID = " + userID);

    }

    @Test
    public void createUserBodyObject() {
        User newUser= new User();
        newUser.setName("Arif");
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userID = given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .body(newUser)
                .log().body()

                .when()
                .post("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(201)
                //.log().body()
                .extract().jsonPath().getInt("data.id");

        System.out.println("userID = " + userID);

    }


}
