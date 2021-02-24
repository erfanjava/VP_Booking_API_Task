package com.vp.bookingApp.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Booking_Util {


    /**
     *
     * @return a random payload using JavaFaker
     */
    public static Map<String,Object> getRandomBookingPayload(){
        Faker fake=new Faker();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String>bookingDate=new LinkedHashMap<>();
        bookingDate.put("checkin",format.format(fake.date().past(365,TimeUnit.DAYS)));
        bookingDate.put("checkout",format.format(fake.date().future(365,TimeUnit.DAYS)));
        Map<String,Object> payload=new LinkedHashMap<>();
        payload.put("firstname",fake.name().firstName());
        payload.put("lastname",fake.name().lastName());
        payload.put("totalprice",fake.number().numberBetween(1,1000));
        payload.put("depositpaid",fake.random().nextBoolean());
        payload.put("bookingdates",bookingDate);
        payload.put("additionalneeds","Provide a "+fake.beer());

        return payload;
    }






    public static Map<String,Object> getFixPayload(){
        Map<String,String>bookingDate=new LinkedHashMap<>();
        bookingDate.put("checkin","2021-02-23");
        bookingDate.put("checkout","2021-02-24");
        Map<String,Object> payload=new LinkedHashMap<>();
        payload.put("firstname","Erfan");
        payload.put("lastname","Oghuz");
        payload.put("totalprice",777);
        payload.put("depositpaid",true);
        payload.put("bookingdates",bookingDate);
        payload.put("additionalneeds","Late Checkout");

        return payload;
    }


}
