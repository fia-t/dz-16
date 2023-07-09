package Booking.Create;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBooking {
    private Number bookingid;
    private booking booking;
}
