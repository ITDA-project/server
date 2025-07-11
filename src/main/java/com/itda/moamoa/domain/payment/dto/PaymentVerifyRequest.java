package com.itda.moamoa.domain.payment.dto;

public record PaymentVerifyRequest(
        String impUid,
        String merchantUid,
        Long somoimId,
        Long sessionId
) {}
