package com.example.Parking.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndParkingResponse {
    private String id;
    private String disabled;
    private String begin;
    private String end;
    private String currencyCode;
    private String value;
}
