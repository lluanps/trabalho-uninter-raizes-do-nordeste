package com.lluanps.raizes_nordeste_api_uninter.exceptions;

public class PaymentServiceUnavailableException extends RuntimeException {

    public PaymentServiceUnavailableException(String mensagem) {
        super(mensagem);
    }

}
