package dev.nano.payment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentConstant {
    public static final String PAYMENT_URI_REST_API = "/api/v1/payments";
    public static final String PAYMENT_NOT_FOUND = "Payment with ID %d not found";
    public static final String PAYMENT_CREATE_ERROR = "Failed to create payment: %s";
    public static final String NO_PAYMENTS_FOUND = "No payments found";
}
