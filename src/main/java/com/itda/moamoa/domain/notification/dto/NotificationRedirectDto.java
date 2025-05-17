package com.itda.moamoa.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRedirectDto {
    private String redirectUrl;
    private Long targetId; //url의 PathVariable

}
