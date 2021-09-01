package goRest;

import goRest.Model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

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

}
