package dev.nano.customer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerConstant {
    public static final String CUSTOMER_URI_REST_API = "/api/v1/customers";
    public static final String CUSTOMER_NOT_FOUND = "Customer with ID %d not found";
    public static final String CUSTOMER_EMAIL_EXISTS = "Customer with email %s already exists";
    public static final String NO_CUSTOMERS_FOUND = "No customers found";
}

