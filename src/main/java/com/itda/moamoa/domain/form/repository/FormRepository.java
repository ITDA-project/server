package com.itda.moamoa.domain.form.repository;

import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Arrays;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Arrays findByPost(Post post);
}