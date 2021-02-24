package com.vp.bookingApp.utils;

import io.restassured.http.ContentType;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Token {


    public static String getToken(){

        Map<String,String>credential=new LinkedHashMap<>();
        credential.put("username",ConfigurationReader.getProperty("username"));
        credential.put("password",ConfigurationReader.getProperty("password"));


        return given()
                .contentType(ContentType.JSON)
                .body(credential).

        when()
                .post("/auth").
        then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token",is(not(emptyString())))
                .extract().jsonPath().getString("token");
    }

}
