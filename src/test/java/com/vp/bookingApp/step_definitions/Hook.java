package com.vp.bookingApp.step_definitions;

import com.vp.bookingApp.utils.ConfigurationReader;
import com.vp.bookingApp.utils.Token;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hook {
    @Before
    public void setupAPI(){

        RestAssured.baseURI= ConfigurationReader.getProperty("baseURI");
        System.out.println("API is being setup");

    }

    @After
    public void destroyAPI(){
        RestAssured.reset();
        System.out.println("API is being terminated");

    }
}
