package com.example.Parking.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeginParkingResponse {
    private String id;
    private String disabled;
    private String begin;
}
