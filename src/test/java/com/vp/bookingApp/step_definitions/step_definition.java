package com.vp.bookingApp.step_definitions;

import com.vp.bookingApp.utils.Booking_Util;
import com.vp.bookingApp.utils.Rest_Util;
import com.vp.bookingApp.utils.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import static com.vp.bookingApp.utils.Booking_Util.getFixPayload;
import static com.vp.bookingApp.utils.Rest_Util.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.*;

public class step_definition {
    static Map<String,Object> payload;
    static Map<String,Object>updatedPayload;
    static Response post_Response;
    static Response put_Response;
    static Response delete_Response;
    static Response invalid_put_Response;
    static Response test_data_Response;
    static String token="token="+Token.getToken();
    static List<Integer> bookingIdList;
    static int bookingID;


    @Given("User's booking information")
    public void user_booking_information() {
        payload = Booking_Util.getRandomBookingPayload();
    }

    @When("User creates a booking")
    public void user_creates_a_booking() {
        post_Response = getPostResponse(payload);
    }

    @Then("The booking should be successful")
    public void the_booking_should_be_successful() {
        post_Response
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("booking.firstname",is(payload.get("firstname")))
                .body("booking.lastname",is(payload.get("lastname")))
                .body("booking.totalprice",is(payload.get("totalprice")))
                .body("booking.depositpaid",is(payload.get("depositpaid")))
                .body("booking.bookingdates",is(payload.get("bookingdates")))
                .body("booking.additionalneeds",is(payload.get("additionalneeds")));

        given()
                .log().uri()
                .contentType(ContentType.JSON)
                .header("Cookie",token)
                .pathParam("id",Rest_Util.get_id(payload)).
        when()
                .delete("/booking/{id}").then().statusCode(201);

    }

    @Given("User with valid booking")
    public void userWithValidBooking() {
        payload=Booking_Util.getRandomBookingPayload();
        Response response=getPostResponse(payload);
        response.then().assertThat().statusCode(200).body(is(not(emptyString())));



    }


    @When("User updates information for existing booking")
    public void userUpdatesInformationForExistingBooking() {
        updatedPayload=Booking_Util.getRandomBookingPayload();
        int id=Rest_Util.get_id(updatedPayload);

        put_Response= given()
                               .log().uri()
                               .pathParam("id",id)
                               .contentType(ContentType.JSON)
                               .header("Cookie",token)
                               .body(updatedPayload).log().body().
                      when()
                                .put("booking/{id}");
    }

    @Then("Verify the information updated successfully")
    public void verifyTheInformationUpdatedSuccessfully() {
        put_Response.then().assertThat()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("firstname",is(updatedPayload.get("firstname")))
                    .body("lastname",is(updatedPayload.get("lastname")))
                    .body("totalprice",is(updatedPayload.get("totalprice")))
                    .body("depositpaid",is(updatedPayload.get("depositpaid")))
                    .body("bookingdates",is(updatedPayload.get("bookingdates")))
                    .body("additionalneeds",is(updatedPayload.get("additionalneeds")));

    }

    @When("user updates information to an invalid booking")
    public void userUpdatesInformationToAnInvalidBooking() {
        int invalidBookID=Integer.MAX_VALUE;
        invalid_put_Response=given()
                                    .log().uri()
                                    .contentType(ContentType.JSON)
                                    .pathParam("id",invalidBookID)
                                    .header("Cookie",token)
                                    .body(Booking_Util.getRandomBookingPayload()).
                             when()
                                    .put("/booking/{id}");
    }


    @Then("user should see the error message")
    public void userShouldSeeTheErrorMessage() {
        invalid_put_Response.then().assertThat().statusCode(405);

    }

    @When("User list all booking information")
    public void userListAllBookingInformation() {
       bookingIdList=given()
                            .log().uri()
                            .contentType(ContentType.JSON).
                    when()
                            .get("/booking").
                    then()
                            .assertThat()
                            .statusCode(200)
                            .extract().jsonPath().getList("bookingid");

    }

    @Then("All the booking information should be returned")
    public void allTheBookingInformationShouldBeReturned() {
        Set<Integer> BookingIDs=new TreeSet<>(bookingIdList);
        System.out.println("BookingIDs = " + BookingIDs);
    }


    @When("User search for booking with name {string}")
    public void userSearchForBookingWith(String filter) {
        if(filter.contains("&")){
            String[] split = filter.split("&");
            String[] firstName=split[0].split("=");
            String[] lastName=split[1].split("=");
            bookingIdList=given()
                                .log().uri()
                                .contentType(ContentType.JSON)
                                .queryParam(firstName[0],firstName[1])
                                .queryParam(lastName[0],lastName[1]).
                            when()
                                .get("/booking").
                            then()
                                .extract().jsonPath().getList("bookingid");
            System.out.println("The bookID i found from this search: "+bookingIdList);

        }else{
            String[] filters=filter.split("=");

        bookingIdList=given()
                           .log().uri()
                           .contentType(ContentType.JSON)
                           .queryParam(filters[0],filters[1]).
                    when()
                           .get("/booking").then().extract().jsonPath().getList("bookingid");
                    }




    }



    @When("User check booking information via {string} id")
    public void userCheckBookingInformationViaId(String condition) {
        if(condition.equals("Valid")){
        bookingID=get_id(payload);
        }else{
            bookingID=Integer.MAX_VALUE;

        }

    }

    @Then("User should able to delete the booking")
    public void userShouldSeeTheBookingInformation() {
        if(bookingID==Integer.MAX_VALUE){
            delete_Response.then().assertThat().statusCode(405);
        }else{
        delete_Response.then().assertThat()
                            .statusCode(201)
                            .body(is("Created"));

        }
    }

    @Then("All returned result should have {string}")
    public void allReturnedResultShouldHave(String result) {
        if(result.equals("[]")){
            Assert.assertEquals(bookingIdList.toString(),"[]");
        }

        if(result.contains("&")){
            String[] split=result.split("&");
            String[] firstName=split[0].split("=");
            String[] lastName=split[1].split("=");

            for(Integer each: bookingIdList) {
                getGetResponse(each).then().assertThat()
                        .statusCode(200)
                        .body(firstName[0], is(firstName[1]))
                        .body(lastName[0], is(lastName[1]));
            }

        }else{
            String [] results=result.split("=");
            for (Integer eachBook : bookingIdList) {
                getGetResponse(eachBook).then().assertThat()
                                        .statusCode(200)
                                        .body(results[0],is(results[1]));
            }

        }
    }

    @Given("User with existing booking")
    public void userWithExistingBooking() {
        test_data_Response=getPostResponse(getFixPayload());
        test_data_Response.then().assertThat().statusCode(200);



    }

    @When("User search for booking with booking {string}")
    public void userSearchForBookingWithBooking(String date) {
        String[] dates=date.split("&");
        String[] checkin=dates[0].split("=");
        String[] checkout=dates[1].split("=");

        bookingIdList=given()
                            .log().uri()
                            .contentType(ContentType.JSON)
                            .queryParam(checkin[0],checkin[1])
                            .queryParam(checkout[0],checkout[1]).
                    when()
                            .get("/booking").
                    then().statusCode(200).extract().jsonPath().getList("bookingid");
        }



    @Then("All returned result should have booking {string}")
    public void allReturnedResultShouldHaveBooking(String result) {
        if(result.equals("[]")){
            Assert.assertEquals(bookingIdList.toString(),"[]");
        }else {

            String[] date_query = result.split("&");
            String[] checkin = date_query[0].split("=");
            String[] checkout = date_query[1].split("=");
            for (Integer eachBooking : bookingIdList) {
                getGetResponse(eachBooking).then().assertThat()
                        .statusCode(200)
                        .body("bookingdates." + checkin[0], is(checkin[1]))
                        .body("bookingdates." + checkout[0], is(checkout[1]));

            }
        }


    }

    @And("User delete the booking with the id")
    public void userDeleteTheBookingWithTheId() {
        delete_Response=given()
                                .log().uri()
                                .contentType(ContentType.JSON)
                                .pathParam("id",bookingID)
                                .header("Cookie",token).
                        when()
                                .delete("/booking/{id}");
    }



}
