package com.itda.moamoa.domain.form.service;

import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.repository.FormRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service        // Service Annotation
public class FormApiService {
    @Autowired  // 의존성 주입 Annotation
    private FormRepository formRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // 신청폼 전체 조회

    // 신청폼 개별 조회


    // 신청폼 생성
    @Transactional     // Transation Annotation
    public FormResponseDTO create(FormRequestDTO requestDTO){
        // 1. User / Post 조회
        User userId = userRepository.findById(requestDTO.getUserId())   // 예외처리 1. 존재하지 않는 사용자의 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post postId = postRepository.findById(requestDTO.getPostId())   // 예외처리 2. 존재하지 않는 게시글에 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        // 2. 신청폼 중복 확인
        if (formRepository.existsById(userId, postId)) {
            throw new IllegalStateException("이미 신청폼을 제출하셨습니다.");
        }

        // 3. 신청폼 Entity 생성 - Builder 적용
        Form form = Form.builder()
                .content(requestDTO.getContent())
                .user(userId)
                .post(postId)
                .build();

        // 4. 신청폼 Entity -> Repository 저장
        Form createdForm = formRepository.save(form);

        // 5. FormResponseDTO 생성 및 반환
        return new FormResponseDTO(createdForm.getId(), createdForm.getContent(), createdForm.getPost().getPost_id(), createdForm.getUser().getId());
    }
}
