package de.aha.backend.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfo {
    private String phone;
    private Address address;
    private boolean allowHouseVisits;
}
