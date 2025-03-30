package com.itda.moamoa.domain.user.entity;

import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@ToString
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true) //한 사용자는 한 이메일 사용
    private String email; //varchar

    private String name; //한 사용자는 동일한 이름을 가질 수 있음(본명)

    @Column(unique = true)
    private String username; //username(유일), password로 로그인

    private String role;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsDiv snsDiv; //naver, kakao

    private String phonenumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String image; //image url
    
    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    @Builder.Default
    private Double ratingAverage = 0.0; // 평균 별점

    // 이미지만 업데이트하는 별도 메서드 (setter 대신)
    public void updateImage(String imageUrl) {
        this.image = imageUrl;
    }
    
    // 리뷰가 추가될 때 평균 별점 업데이트
    public void addReview(Double rating) {
        if (ratingAverage == 0.0) {
            // 첫 리뷰인 경우
            this.ratingAverage = rating;
        } else {
            // 기존에 리뷰가 있는 경우 (단순 이동 평균)
            this.ratingAverage = (this.ratingAverage + rating) / 2;
        }
    }
}
