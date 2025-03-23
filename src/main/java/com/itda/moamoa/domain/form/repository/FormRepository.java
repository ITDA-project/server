package com.itda.moamoa.domain.form.repository;

import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository                                                             // Repository Annotation
public interface FormRepository extends JpaRepository<Form, Long> {     // JPA Annotation 상속 -> CRUD, Pagging, JAP 지원
    boolean existsById(User userId, Post postId);    // if (userId != null && postId != null)
}