package dev.ao.example.models;


import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;


@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Builder
public class Address extends UpdatableEntity {
//    TODO add restrictions with @annotations

    private String country;
    @Column(name = "country_code")
    private String countryCode;
    private String state;

    @Column(name = "street_name")
    private String streetName;
    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "house_number")
    private Integer houseNumber;
    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
    private String latitude;
    private String longitude;
    private String timezone;// TODO: refrance timezone for timeslots and add if country has daytime saving
}