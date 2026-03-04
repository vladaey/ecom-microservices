package com.jeretin.ecommerce.user.models;

import lombok.Data;


//@Entity(name="addresses")
@Data
///*@NoArgsConstructor
//@AllArgsConstructor*/
public class Address {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
