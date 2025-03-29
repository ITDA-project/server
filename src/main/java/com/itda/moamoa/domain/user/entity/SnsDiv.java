package com.itda.moamoa.domain.user.entity;

public enum SnsDiv {
    KAKAO("kakao"), NAVER("naver");

    private String value;

    SnsDiv(String value){
        this.value = value;
    }

    public static SnsDiv fromString(String value){
        for(SnsDiv snsDiv : SnsDiv.values()){
            if(snsDiv.value.equalsIgnoreCase(value)){
                return snsDiv;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 소셜입니다 " + value);
    }
}
