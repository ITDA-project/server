package com.itda.moamoa.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentStatusRequestDto {
    private Long roomId;
    private List<Long> userIds;
} 