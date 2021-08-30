import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Task {
    /**
     * Task 1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void task1() {
        given()
                .when()
                .get("https://httpstat.us/203")

                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }

    /*Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */
    @Test
    public void task2() {
        given()
                .when()
                .get("https://httpstat.us/203")

                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"));

        //second yontem
        String sonuc =
                given()
                        .when()
                        .get("https://httpstat.us/203")

                        .then()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                        .extract().body().toString();
        Assert.assertTrue(sonuc.equalsIgnoreCase("203 Non-Authoritative Information"));
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void task3() {
        String title =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().body().jsonPath().get("title");
        Assert.assertTrue(title.equalsIgnoreCase("quis ut nam facilis et officia qui"));
    }

    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */
    @Test
    public void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("completed", equalTo(false))
        ;

        boolean completed=given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("completed", equalTo(false))
                .extract().path("completed")
        ;
        Assert.assertFalse(completed);
    }












}



