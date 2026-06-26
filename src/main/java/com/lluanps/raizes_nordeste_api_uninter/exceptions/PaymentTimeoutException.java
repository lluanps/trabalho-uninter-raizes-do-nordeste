package com.lluanps.raizes_nordeste_api_uninter.exceptions;

public class PaymentTimeoutException extends RuntimeException {

    public PaymentTimeoutException(String mensagem) {
        super(mensagem);
    }

}
