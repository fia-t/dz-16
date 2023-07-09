import Booking.Create.ResponseBooking;
import Booking.Create.createBooking;
import Booking.Create.createBookingdates;
import Booking.UpdateBooking.ResponseUpdateBooking;
import Booking.UpdateBooking.UpdateBooking;
import Booking.UpdateBooking.updateBookingdates;
import Booking.UpdateBookingPrice.ResponsePartialUpdateBookingPrice;
import Booking.UpdateBookingPrice.UpdateBookingPrice;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestAssuredTests {
    private RequestSpecification specification;
    private RequestSpecification specificationUpdate;
    private RequestSpecification specificationDelete;
    private String createdBookingId;
    private Number totalprice;
    private Integer firstBookingId;


    @BeforeMethod
    public void setup(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/booking";
        specification =  RestAssured.given()
                .contentType("application/json")
                .accept("application/json");

        specificationUpdate = RestAssured.given()
                .contentType("application/json")
                .accept("application/json")
                .cookie("token=abc12")
                .headers("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");

        specificationDelete = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");
    }

    @Test
    public void createBooking(){

        java.util.Date checkin = null;
        java.util.Date checkout = null;
        try {
            checkin = new SimpleDateFormat("yyyy-MM-dd").parse("2023-07-10");
            checkout = new SimpleDateFormat("yyyy-MM-dd").parse("2023-07-15");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        createBookingdates bookingDates = new createBookingdates(checkin, checkout);

        createBooking body = new createBooking().builder()
                .firstname("firstTestName")
                .lastname("lastTestName")
                .totalprice(150)
                .depositpaid(true)
                .bookingdates(bookingDates)
                .additionalneeds("Breakfast")
                .build();

        Response createBooking = specification
                .body(body)
                .post();
        createBooking.prettyPrint();
        createBooking.as(ResponseBooking.class);
        createBooking.then().statusCode(200);

        createdBookingId = createBooking.jsonPath().getString("bookingid");

    }

    @Test(dependsOnMethods = "createBooking")
    public void getBookingIds(){
       Response responseGetBookingIds = RestAssured.get();
       responseGetBookingIds.then().statusCode(200);
       responseGetBookingIds.prettyPrint();

       JsonPath jsonPath = responseGetBookingIds.jsonPath();
       List<String> bookingIds = jsonPath.getList("bookingid");
       Assert.assertTrue(bookingIds.contains(Integer.parseInt(createdBookingId)));

       firstBookingId = Integer.valueOf(jsonPath.getString("[0].bookingid"));
       System.out.println("Значення першого bookingid: " + firstBookingId);
    }

    @Test(dependsOnMethods = "getBookingIds")
    public void updateBookingChangePrice(){

        UpdateBookingPrice body = new UpdateBookingPrice().builder()
                .totalprice(300)
                .build();

        Response UpdateBookingPrice = specificationUpdate
                .body(body).log().all()
                .patch("/"+createdBookingId);
        UpdateBookingPrice.prettyPrint();
        UpdateBookingPrice.as(ResponsePartialUpdateBookingPrice.class);
        UpdateBookingPrice.then().statusCode(200);
        totalprice = UpdateBookingPrice.jsonPath().get("totalprice");
        Assert.assertEquals(totalprice, 300);
    }

    @Test(dependsOnMethods = "getBookingIds")
    public void updateBooking(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date checkinForUp = null;
        java.util.Date checkoutForUp = null;
        try {
            checkinForUp = dateFormat.parse("2023-07-11");
            checkoutForUp = dateFormat.parse("2023-07-16");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        updateBookingdates bookingdatesForUp = new updateBookingdates(dateFormat.format(checkinForUp), dateFormat.format(checkoutForUp));

        UpdateBooking body = new UpdateBooking().builder()
                .firstname("Ilon")
                .lastname("Mask")
                .totalprice(150)
                .depositpaid(false)
                .bookingdates(bookingdatesForUp)
                .additionalneeds("Diner")
                .build();

        Response updateBooking = specificationUpdate
                .body(body).log().all()
                .put("/"+firstBookingId);
        updateBooking.prettyPrint();
        updateBooking.as(ResponseUpdateBooking.class);
        updateBooking.then().statusCode(200);
        String firstname = updateBooking.jsonPath().get("firstname");
        String lastname = updateBooking.jsonPath().get("lastname");
        String additionalneeds = updateBooking.jsonPath().get("additionalneeds");
        System.out.println(firstname);
        System.out.println(lastname);
        System.out.println(additionalneeds);
        Assert.assertEquals(firstname, "Ilon");
        Assert.assertEquals(lastname, "Mask");
        Assert.assertEquals(additionalneeds, "Diner");
    }

    @Test(dependsOnMethods = "updateBookingChangePrice")
    public void deleteCreatedBooking(){
        Response responseDeleteCreatedBooking = specificationDelete
                .log().all()
                .delete("/"+createdBookingId);

        responseDeleteCreatedBooking.then().statusCode(201);
        responseDeleteCreatedBooking.prettyPrint();

        Response responseGetBookingIds = RestAssured.get();
        JsonPath jsonPath = responseGetBookingIds.jsonPath();
        List<String> bookingIds = jsonPath.getList("bookingid");
        Assert.assertFalse(bookingIds.contains(Integer.parseInt(createdBookingId)));
    }




}
