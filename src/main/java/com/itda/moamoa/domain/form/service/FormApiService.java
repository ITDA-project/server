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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service                    // Service
@RequiredArgsConstructor    // 필수 필드 생성자
public class FormApiService {
    private final FormRepository formRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 해당 게시글에 제출된 신청폼 전체 조회
    public List<Form> getAllForms(long postid, FormRequestDTO requestDto) {
        // 1. User / Post 조회
        User user = userRepository.findById(requestDto.getUser())   // 예외처리 1. 회원이 아닌 사용자의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postid)                 // 예외처리 2. 아직 신청서가 제출되지 않은 게시글의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("아직 신청폼이 제출되지 않았습니다."));

        // 3. 권한이 없는 사용자(Not Host)의 신청폼 전체 조회 요청


        // 4. 조회된 신청폼 전체 반환
        return formRepository.findAll();
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    public Form getFormById(long postid, long formid, FormRequestDTO requestDto) {
        // 1. User / Post 조회
        User user = userRepository.findById(requestDto.getUser())   // 예외처리 1. 회원이 아닌 사용자의 게시글 신청폼 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("신청폼 열람 권한이 존재하지 않습니다."));
        Post post = postRepository.findById(postid)                 // 예외처리 2. 존재하지 않는 게시글의 신청폼 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 예외처리 3. 권한이 없는 사용자의 신청폼 조회 요청

        // 예외처리 4. 인증이 만료된 사용자의 신청폼 조회 요청


        // 5. 조회된 신청폼 반환
        return formRepository.findById(formid)      // 예외처리 6. 존재하지 않는 신청품 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 신청폼이 존재하지 않습니다."));
    }

    // 신청폼 생성
    @Transactional     // Transation
    public FormResponseDTO create(FormRequestDTO requestDto){
        // 1. User / Post 조회
        User user = userRepository.findById(requestDto.getUser())   // 예외처리 1. 존재하지 않는 사용자의 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = postRepository.findById(requestDto.getPost())   // 예외처리 2. 존재하지 않는 게시글에 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        // 2. 신청폼 내용 누락 확인
        if (requestDto.getContent() == null)
            throw new IllegalStateException("신청폼 양식이 채워지지 않았습니다."); // 예외처리 3. 신청폼 내용이 누락된 경우

        // 3. 신청폼 중복 확인
        if (formRepository.existsByUserAndPost(user, post))
            throw new IllegalStateException("이미 신청폼을 제출하셨습니다.");    // 예외처리 4. 신청품을 이미 제출한 경우

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
