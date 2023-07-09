package Booking.UpdateBooking;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUpdateBooking {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private updateBookingdates bookingdates;
    private String additionalneeds;
}
