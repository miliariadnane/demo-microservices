package dev.nano.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseController {
    public static final String CUSTOMER_TAG = "Customer Management";
    public static final String CUSTOMER_DESCRIPTION = "Operations about customers including orders and payments";
    public static final String PRODUCT_TAG = "Product Management";
    public static final String PRODUCT_DESCRIPTION = "Operations for managing products catalog and inventory";
    public static final String ORDER_TAG = "Order Management";
    public static final String ORDER_DESCRIPTION = "Operations for managing customer orders and order processing";
    public static final String PAYMENT_TAG = "Payment Management";
    public static final String PAYMENT_DESCRIPTION = "Operations for managing payments and transactions";
    public static final String NOTIFICATION_TAG = "Notification Management";
    public static final String NOTIFICATION_DESCRIPTION = "Operations for managing notifications and communication with customers";
    public static final String API_KEY_TAG = "API Key Management";
    public static final String API_KEY_DESCRIPTION = "Operations for managing API keys and their authorizations";
    public static final String APPLICATION_TAG = "Application Management";
    public static final String APPLICATION_DESCRIPTION = "Operations for managing application access and permissions";
}
