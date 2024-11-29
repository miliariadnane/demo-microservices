package dev.nano.order;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderConstant {
    public static final String ORDER_URI_REST_API = "/api/v1/orders";
    public static final String ORDER_NOT_FOUND = "Order with ID %d not found";
    public static final String ORDER_CREATE_ERROR = "Failed to create order: %s";
    public static final String NO_ORDERS_FOUND = "No orders found";
}

