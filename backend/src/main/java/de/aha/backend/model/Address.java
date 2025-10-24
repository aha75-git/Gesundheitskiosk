package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String houseNumber;
}
