package com.itda.moamoa.domain.form.repository;

import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.entity.FormStatus;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    boolean existsByUserAndPost(User user, Post post);

    List<Form> findByPostAndFormStatusAndFormIdLessThanOrderByFormIdDesc(Post post, FormStatus formStatus, Long cursor, Pageable pageable);
    
    // 특정 사용자가 신청한 모든 form 조회
    List<Form> findByUser(User user);

    // 사용자 신청서 조회
    Optional<Form> findByUserAndPost(User user, Post post);
}