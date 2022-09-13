package dev.nano.product;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {
    private long id;
    private String name;
    private String image;
    private int price;
}
