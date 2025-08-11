package com.itda.moamoa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponseDto {
    private Long sessionId;
    private List<UserPaymentStatus> userPaymentStatuses;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPaymentStatus {
        private Long userId;
        private boolean isPaid;
    }
} 