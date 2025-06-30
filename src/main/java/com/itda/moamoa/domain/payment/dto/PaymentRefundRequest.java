package com.itda.moamoa.domain.payment.dto;

public record PaymentRefundRequest(
        String impUid,
        int amount
) {}
