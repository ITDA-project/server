package com.itda.moamoa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusRequestDto {
    private Long roomId;
    private Long sessionId;
} 