package com.example.Parking.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndParkingRequest {
    private String id;
    private String currencyCode;
}
