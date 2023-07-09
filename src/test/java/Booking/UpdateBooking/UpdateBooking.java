package Booking.UpdateBooking;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBooking {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private updateBookingdates bookingdates;
//    private Date checkin;
//    private Date checkout;
    private String additionalneeds;
}
