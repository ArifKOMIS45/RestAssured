import POJO.JsonPlaceHolder;
import io.restassured.http.ContentType;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

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

        boolean completed = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("completed", equalTo(false))
                .extract().path("completed");
        Assert.assertFalse(completed);
    }

    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     * title = "fugiat veniam minus"
     * userId = 1
     */

    @Test
    public void task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title[2]", equalTo("fugiat veniam minus"))
                .body("userId[2]", equalTo(1));
    }

    /**
     * Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void task6() {
        JsonPlaceHolder jsonPlaceHolder =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .extract().as(JsonPlaceHolder.class);

        System.out.println("jsonPlaceHolder " + jsonPlaceHolder);
        System.out.println("jsonPlaceHolder.getUserID() = " + jsonPlaceHolder.getUserId());

    }

    /**
     * Task 7
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into Array of POJOs
     */
    @Test
    public void task7() {

        JsonPlaceHolder[] js =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")

                        .then()
                        .statusCode(200)
                        .extract().as(JsonPlaceHolder[].class);
        System.out.print("js = " + Arrays.toString(js));

    }
/** Task 8
 * create a request to https://jsonplaceholder.typicode.com/todos
 * expect status 200
 * Converting Array Into List of POJOs
 */
@Test
public void task8() {

JsonPlaceHolder[] js =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")

                .then()
                .statusCode(200)
                .extract().as(JsonPlaceHolder[].class);
List<JsonPlaceHolder> list= Arrays.asList(js);
    for (int i = 0; i <list.size() ; i++) {
        System.out.println(list.get(i));
    }
        
    }
   


}



