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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                    // Service
@RequiredArgsConstructor    // 필수 필드 생성자
public class FormApiService {
    private final FormRepository formRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 해당 게시글에 제출된 신청폼 전체 조회
    public List<Form> getAllForms(long postId, String username, FormRequestDTO requestDto) {
        // 1. User / Post 조회
        User user = userRepository.findByUsername(username)        // 예외처리 1. 회원이 아닌 사용자의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 아직 신청서가 제출되지 않은 게시글의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("아직 신청폼이 제출되지 않았습니다."));

        // 2. 예외처리 3. 권한이 없는 사용자(Not Host)의 신청폼 전체 조회 요청
        if (!post.getUser().equals(username))                 // 신청자 조회를 요청한 사용자 != 작성자 Token
            throw new IllegalStateException("신청서를 열람할 권한이 없습니다.");

        // 3. 조회된 신청폼 전체 반환
        return formRepository.findAll();
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    public Form getFormById(long postId, long formId, String username, FormRequestDTO requestDto) {
        // 1. User / Post 조회
        User user = userRepository.findByUsername(username)        // 예외처리 1. 회원이 아닌 사용자의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)                // 예외처리 2. 존재하지 않는 게시글의 신청폼 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 예외처리 3. 권한이 없는 사용자(Not host)의 신청폼 조회 요청
        if (!post.getUser().equals(username))                 // 신청자 조회를 요청한 사용자 != 작성자 Token
            throw new IllegalStateException("신청서를 열람할 권한이 없습니다.");

        // 3. 조회된 신청폼 반환
        return formRepository.findById(formId)      // 예외처리 6. 존재하지 않는 신청품 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 신청폼이 존재하지 않습니다."));
    }

    // 신청폼 생성
    @Transactional     // Transaction
    public FormResponseDTO create(String username, long postId, FormRequestDTO requestDto){
        // 1. User / Post 조회
        User user = userRepository.findByUsername(username)        // 예외처리 1. 회원이 아닌 사용자의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 존재하지 않는 게시글에 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        // 2. 예외처리 3. 신청폼 내용 누락된 경우
        if (requestDto.getContent() == null)
            throw new IllegalStateException("신청폼 양식이 채워지지 않았습니다.");

        // 3. 예외처리 4. 해당 게시글에 신청폼을 이미 제출한 경우
        // Username token 사용
        if (formRepository.existsByUserAndPost(user, post))
            throw new IllegalStateException("이미 신청폼을 제출하셨습니다.");

        // 4. 신청폼 Entity 생성 - ModelMapper 적용
        Form form = modelMapper.map(requestDto, Form.class);
        form.setUser(user);     // 연관 관계
        form.setPost(post);

        // 5. 신청폼 DB 저장
        Form createdForm = formRepository.save(form);

        // 6. FormResponseDTO 생성 및 반환
        return modelMapper.map(createdForm, FormResponseDTO.class);
    }
}
