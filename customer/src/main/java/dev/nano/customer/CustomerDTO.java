package dev.nano.customer;


import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDTO implements Serializable {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
