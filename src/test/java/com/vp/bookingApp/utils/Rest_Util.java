package com.vp.bookingApp.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class Rest_Util {


    public static int get_id(Map<String,Object> payload) {


        Response response = given()
                                .contentType(ContentType.JSON)
                                .body(payload).
                            when()
                                .post("/booking");
        return response.then().extract().jsonPath().getInt("bookingid");




    }

    public static Response getPostResponse(Map<String,Object> payload){
        return given()
                    .contentType(ContentType.JSON)
                    .body(payload).
                when()
                    .post("/booking");
    }

    public static Response getGetResponse(int id){
        return given()
                    .contentType(ContentType.JSON)
                    .pathParam("id",id).
               when()
                    .get("/booking/{id}");
    }


}
