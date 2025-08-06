package com.itda.moamoa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoRequestDto {
    private Long somoimId; 
    private Long sessionId;
    private Long userId;
} 