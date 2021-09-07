package goRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GoRestCommentsTest {
    // Task 1: https://gorest.co.in/public/v1/comments  Api sinden dönen verilerdeki data yı bir nesne yardımıyla
    //         List olarak alınız.

    int id = 0;

    //Task 2=  Bütün Comment lardaki emailleri bir list olarak alınız ve
    // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.

    @Test(enabled = false)
    public void getGoRestComments() {
        List<Data> dataList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Data.class);

        System.out.println("userCommentsList = " + dataList);
    }

    @Test(enabled = false)
    public void getGoRestCommentsEmails() {
        List<String> userCommentsEmailList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getList("data.email");

        System.out.println("userCommentsList = " + userCommentsEmailList);
        Assert.assertTrue(userCommentsEmailList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test(enabled = false)
    public void getGoRestCommentsEmails2() {
        //data[0].email demek birinci email
        //Butun emailler icin data.email dememiz gerek
        List<String> userCommentsEmailList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        // .log().body()
                        .extract().path("data.email")//Stringden olusan bir list geliyor o yuzden liste esitlemeli
                ;

        for (String s : userCommentsEmailList) {
            System.out.println("email = " + s);
        }
        Assert.assertTrue(userCommentsEmailList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test(enabled = false)
    public void getGoRestCommentsEmails3() {
        //data[0].email demek birinci email
        //Butun emailler icin data.email dememiz gerek
        Response CommentList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        // .log().body()
                        .extract().response();

        List<String> emailList = CommentList.path("data.email");//bu ikiside ayni listi veriyor
        List<String> emailList2 = CommentList.jsonPath().getList("data.email", String.class);

        List<Integer> IDs = CommentList.path("data.id");

        System.out.println("emailList = " + emailList);
        System.out.println("IDs = " + IDs);
        System.out.println("emailList2 = " + emailList2);

        Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
        Assert.assertTrue(emailList2.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test(enabled = false)
    public void getCommentsPojo() {

        CommentsBody commentsBody =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .extract().as(CommentsBody.class);


        System.out.println("goRestCommentsAllList = " + commentsBody.getData().get(5).getEmail());
        System.out.println("goRestCommentsAll.getMeta().getPagination().getLinks().getCurrent() = " + commentsBody.getMeta().getPagination().getLinks().getCurrent());

    }

    @Test
    public void getCommentsPost() {
        Data user = new Data();
       user.setEmail("arpali@gmail.com");
        user.setName("Ara2");
        user.setBody("2d");
        id =
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("https://gorest.co.in/public/v1/posts/68/comments")

                .then()
                .statusCode(201)
                .log().body()
        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("id = " + id);
    }

    @Test(dependsOnMethods = "getCommentsPost",priority = 1)
    public void getCommentsPut() {
        Data user = new Data();
        user.setEmail("arpali@gmail.com");
        user.setName("Ara2");
        user.setBody("kim cin");//update yapilan yer
        user.setPost_id(68);


        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .contentType(ContentType.JSON)
                .pathParam("id",id)
                .body(user)
                .when()
                .put("https://gorest.co.in/public/v1/comments/{id}")
                .then()
                //.statusCode(200)
                .log().body()
        ;
    }

    @Test(priority = 2)
    public void getCommentsDelete() {
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("id",id)
                .when()
                .delete("https://gorest.co.in/public/v1/comments/{id}")
                .then()
                .statusCode(204)

        ;
    }
    @Test(priority = 3)
    public void getCommentsDeleteNegative() {
        given()
                .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                .pathParam("id",id)
                .when()
                .delete("https://gorest.co.in/public/v1/comments/{id}")
                .then()
                .statusCode(404)

        ;
    }



}
