package com.itda.moamoa.global.fcm.dto;

public enum NotificationType {
    // Form
    FORM_APPLY,
    FORM_APPROVED,
    FORM_REJECTED,

    // Payment
    PAYMENT_REQUESTED,
    PAYMENT_COMPLETED;

//    public String getRedirectUrl(Long relatedId) {
//        return switch (this) {
//            case PAYMENT_REQUESTED -> "/api/chatroom";
//            case PAYMENT_COMPLETED ->  "/api/payments/verify";
//            // case FORM_APPROVED     -> "/chat/" + relatedId;
//            case FORM_REJECTED     -> "/api/posts/list";
//            case FORM_APPLY        -> "/api/posts/{postId}/form/list";
//        };
//    }
}