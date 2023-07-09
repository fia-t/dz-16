package Booking.Create;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class createBooking {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private createBookingdates bookingdates;
    private String additionalneeds;
}
