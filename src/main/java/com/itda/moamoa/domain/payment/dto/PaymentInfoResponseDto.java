package com.itda.moamoa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoResponseDto {
    private String impUid;
    private int amount;
    
    public static PaymentInfoResponseDto of(String impUid, int amount) {
        return new PaymentInfoResponseDto(impUid, amount);
    }
} 