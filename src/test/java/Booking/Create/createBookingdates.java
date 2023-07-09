package Booking.Create;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class createBookingdates {
    private Date checkin;
    private Date checkout;
}
