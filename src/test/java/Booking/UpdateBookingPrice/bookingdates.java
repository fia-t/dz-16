package Booking.UpdateBookingPrice;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class bookingdates {
    private Date checkin;
    private Date checkout;
}
