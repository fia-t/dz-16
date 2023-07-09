package Booking.Create;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class booking {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private bookingdates bookingdates;
    private String additionalneeds;
}
