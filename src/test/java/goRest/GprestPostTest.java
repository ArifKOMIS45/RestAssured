package goRest;

import goRest.Model.DataPost;
import goRest.Model.PostBody;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GprestPostTest {

    int id = 0;

    // Task 1 : https://gorest.co.in/public/v1/posts  API sinden dönen data bilgisini bir class yardımıyla
    // List ini alınız.
    @BeforeClass
    public void baseUri() {
        baseURI = "https://gorest.co.in/public/v1";
    }

    @Test
    public void getPost() {
        List<DataPost> dataPostList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/posts")
                        .then()
                        //.log().body()
                        .extract().jsonPath().getList("data", DataPost.class);
        for (DataPost dp : dataPostList) {
            System.out.println("dp = " + dp);
        }
    }

    @Test
    public void getPostForOneUser() {
        List<DataPost> postsList =
                given()
                        .when()
                        .get("/users/87/posts")
                        .then()
                        //.log().body()
                        .extract().jsonPath().getList("data", DataPost.class);

        for (DataPost p : postsList) {
            System.out.println("p= " + p);
        }
    }

    @Test
    public void getCreatePost() {
        DataPost p = new DataPost();
        p.setUser_id(87);
        p.setTitle("Asci4");
        p.setBody("Leziz yemekler3");

        id = given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .body(p)
                .when()
                .post("/posts")
                .then()
                .log().body()
                .statusCode(201)
                .body("data.title", equalTo("Asci4"))
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("id = " + id);

    }

    @Test
    public void getPostAllInfo() {
        PostBody postBody =
                given()
                        .when()
                        .get("/posts")
                        .then()
                        .extract().as(PostBody.class);
        System.out.println("PostAll = " + postBody);
    }


    @Test(dependsOnMethods = "getCreatePost")
    public void getPostIdCheck() {
        given()
                .pathParam("id", id)
                .when()
                .get("/posts/{id}")
                .then()
                .body("data.id", equalTo(id))
                .statusCode(200)

        ;
    }

    @Test(dependsOnMethods = "getCreatePost")
    public void getPostUpdate() {
        DataPost p = new DataPost();
        p.setUser_id(182);
        p.setTitle("Bakkal");
        p.setBody("Sakiz fiyatlari");
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                //.body("{\"user_id\": 21}")
                .body(p)
                .pathParam("id", id)

                .when()
                .put("/posts/{id}")

                .then()
                .body("data.id", equalTo(id))
                .log().body()
                .statusCode(200)

        ;
    }

    @Test(dependsOnMethods = "getPostUpdate")
    public void postDelete() {
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("id", id)

                .when()
                .delete("/posts/{id}")
                .then()
                .log().status()
                .statusCode(204)
        ;

    }


    @Test(dependsOnMethods = "postDelete")
    public void postDeleteNegative() {
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("id", id)

                .when()
                .delete("/posts/{id}")
                .then()
                .log().status()
                .statusCode(404)
        ;

    }

}






