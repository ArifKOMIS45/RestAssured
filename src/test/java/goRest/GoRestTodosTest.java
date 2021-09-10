package goRest;

import goRest.Model.DataTodos;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestTodosTest {
    // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    // zaman olarak ilk todo nun hangi userId ye ait olduğunu bulunuz

    int id = 0;


    // Task 2: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    //         en son todo nun id sini bulunuz

    @Test
    public void getTodosLastDatedID() throws ParseException {
        List<DataTodos> dataTodosList = null;
        for (int i = 1; i < 96; i++) {
            dataTodosList =
                    given()
                            .param("page", i)
                            .when()
                            .get("https://gorest.co.in/public/v1/todos")
                            .then()
                            //.log().body()
                            .extract().jsonPath().getList("data", DataTodos.class);
        }

        List<Date> datesOfCreate = new ArrayList<>();
        //PriorityQueue<Date> dateQ = new PriorityQueue<Date>();
        for (DataTodos d : dataTodosList) {
            String dates = d.getDue_on().substring(0, 10);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dates);
            //dateQ.add(date);
            datesOfCreate.add(date);
        }
        Collections.sort(datesOfCreate);
        // System.out.println("latest date = " + datesOfCreate.get(datesOfCreate.size()-1).toString());

        for (DataTodos d : dataTodosList) {
            if (d.getDue_on().substring(0, 10).equals(datesOfCreate.get(datesOfCreate.size() - 1).toString())) {
                System.out.println("d.getUser_id() = " + d.getUser_id());
            }
        }

        //System.out.println("dateQ = " + dateQ.peek());
    }

    @Test
    public void getTodos() {
        List<DataTodos> dataTodosList = null;
        for (int i = 1; i < 102; i++) {

            dataTodosList =
                    given()
                            .param("page", i)
                            .when()
                            .get("https://gorest.co.in/public/v1/todos")
                            .then()
                            .log().body()
                            .extract().jsonPath().getList("data", DataTodos.class);
        }
        // System.out.println("dataTodosList = " + dataTodosList.get(dataTodosList.size()-1).getId());
        int a = 0;
        for (int i = 0; i < dataTodosList.size() - 1; i++) {
//            if (dataTodosList.get(i).getId() > a) {
//                a = dataTodosList.get(i).getId();   }
            a = Math.max(dataTodosList.get(i).getId(), dataTodosList.get(i + 1).getId());
        }
        System.out.println("max id = " + a);
    }

// Task 3 : https://gorest.co.in/public/v1/todos  Api sinden
    // dönen bütün bütün sayfalardaki bütün idleri tek bir Liste atınız.

    @Test
    public void getTodos2() {


        int totalPage = 0, page = 1, maxId = 1;
        do {

            Response response =
                    given()
                            .param("page", page)// ?page=1
                            .when()
                            .get("https://gorest.co.in/public/v1/todos")

                            .then()
                            //.log().body()
                            .extract().response();
            if (page == 1) {
                totalPage = response.jsonPath().getInt("meta.pagination.pages");
            }

            List<DataTodos> pageList = response.jsonPath().getList("data", DataTodos.class);

            for (int i = 0; i < pageList.size(); i++) {
                if (maxId < pageList.get(i).getId())
                    maxId = pageList.get(i).getId();
            }


            page++;
        } while (!(totalPage == page));//butun sayfalari direk bulduk
        System.out.println("maxId = " + maxId);
    }

    @Test
    public void getTodosAllID() {
        List<DataTodos> dataTodosList = null;
        List<Integer> allId = new ArrayList<>();
        for (int i = 1; i < 96; i++) {
            dataTodosList =
                    given()
                            .param("page", i)
                            .when()
                            .get("https://gorest.co.in/public/v1/todos")
                            .then()
                            //.log().body()
                            .extract().jsonPath().getList("data", DataTodos.class);

            for (DataTodos d : dataTodosList) {
                allId.add(d.getId());
            }
        }

        System.out.println("allId = " + allId);
    }

    @Test
    public void createToDo() {
        Date date = new Date();
        DataTodos create = new DataTodos();
        create.setStatus("pending");
        create.setTitle("Ben id");
        create.setDue_on(String.valueOf(date));
        create.setUser_id(188);

        id =
                given()
                        .header("Authorization", "Bearer 94154d2d141c86ace03fe8bed7d123bbb874b05046eadacf532b3dc60596a006")
                        .contentType(ContentType.JSON)
                        .body(create)

                        .when()
                        .post("https://gorest.co.in/public/v1/todos")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .body("data.status", equalTo("pending"))
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("id = " + id);

    }


}
