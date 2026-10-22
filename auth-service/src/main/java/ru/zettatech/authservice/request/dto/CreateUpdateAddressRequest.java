package ru.zettatech.authservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateAddressRequest {
    private String userName;
    private String addressLine;
    private String city;
    private String pin;
    private String state;
    private String country;
}
