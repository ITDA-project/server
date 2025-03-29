package com.itda.moamoa.global.common;

import java.security.SecureRandom;

public class RandomPasswordGenerator {

    // 비밀번호 길이
    private static final int PASSWORD_LENGTH = 12;

    // 비밀번호에 사용할 문자들
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // 랜덤으로 선택된 문자들로 비밀번호 생성
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }

        return password.toString();
    }
}
