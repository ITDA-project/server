package com.itda.moamoa.domain.form.service;

import com.itda.moamoa.domain.form.dto.FormListResponseDTO;
import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.entity.FormStatus;
import com.itda.moamoa.domain.form.repository.FormRepository;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.domain.participant.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormApiService {
    private final FormRepository formRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // 해당 게시글에 제출된 신청폼 전체 조회
    public List<FormListResponseDTO> getAllForms(long postId, String username) {
        User user = userRepository.findByUsername(username)      // 예외처리 1. 회원이 아닌 사용자의 신청서 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 아직 신청서가 제출되지 않은 게시글의 신청서 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("아직 신청폼이 제출되지 않았습니다."));
        if (!post.getUser().getUsername().equals(username))     // 예외처리 3. 게시글 작성자가 아닌 회원의 신청서 조회 요청
            throw new IllegalStateException("신청서를 열람할 권한이 없습니다.");

        // 임시로 모든 폼을 가져옵니다.
        List<Form> forms = formRepository.findAll();
        
        // 가져온 폼 중에서 해당 게시글에 속한 것만 필터링합니다.
        forms = forms.stream()
                .filter(form -> form.getPost().equals(post))
                .toList();
        
        if (forms.isEmpty()) {
            // 테스트를 위해 예외를 던지지 않고 빈 리스트 반환
            return List.of();
        }

        // Form 엔티티를 FormListResponseDTO로 변환합니다.
        return forms.stream()
                .map(form -> FormListResponseDTO.builder()
                        .content(form.getContent())
                        .userName(form.getUser().getName())
                        .userImage(form.getUser().getImage())
                        .postTitle(post.getTitle())
                        .build())
                .toList();
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    public FormResponseDTO getFormById(long postId, long formId, String username) {
        User user = userRepository.findByUsername(username)     // 예외처리 1. 회원이 아닌 사용자의 신청서 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)            // 예외처리 2. 존재하지 않는 게시글의 신청서 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUser().getUsername().equals(username))     // 예외처리 3. 권한이 없는 사용자(Not host)의 신청폼 조회 요청
            throw new IllegalStateException("신청서를 열람할 권한이 없습니다.");

        Form form = formRepository.findById(formId)             // 예외처리 4. 존재하지 않는 신청폼 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 신청폼이 존재하지 않습니다."));

        return FormResponseDTO.builder()
                .content(form.getContent())
                .userName(form.getUser().getName())
                .userImage(form.getUser().getImage())
                .build();
    }

    // 신청폼 생성
    @Transactional
    public void create(String username, long postId, FormRequestDTO requestDto){
        User user = userRepository.findByUsername(username)      // 예외처리 1. 회원이 아닌 사용자의 신청폼 전체 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 존재하지 않는 게시글에 신청폼 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (requestDto.getContent() == null)                    // 예외처리 3. 신청폼 내용 누락된 경우
            throw new IllegalStateException("신청폼 양식이 채워지지 않았습니다.");
        if (formRepository.existsByUserAndPost(user, post))     // 예외처리 4. 해당 게시글에 신청폼을 이미 제출한 경우
            throw new IllegalStateException("이미 신청폼을 제출하셨습니다.");

        Form form = Form.builder()
                .content(requestDto.getContent())
                .user(user)
                .post(post)
                .build();

        formRepository.save(form);
    }

    // 신청서 수락
    @Transactional
    public void acceptForm(long postId, long formId, String username) {
        User user = userRepository.findByUsername(username)      // 예외처리 1. 작성자가 아닌 회원의 신청서 수락 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 삭제된 게시글의 신청서 수락 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        Form form = formRepository.findById(formId)             // 예외처리 3. 존재하지 않는 신청서를 수락 요청
                .orElseThrow(() -> new IllegalArgumentException("신청폼이 존재하지 않습니다."));

        Participant participant = participantRepository.findByUserUsernameAndPost(username, post);

        form.updateFormStatus(FormStatus.ACCEPT);

        participant.updateRole(Role.PARTICIPANT);
        participant.updateParticipantStatus(ParticipantStatus.ENTER);
    }

    // 신청서 거절
    @Transactional
    public void refuseForm(long postId, long formId, String username) {
        User user = userRepository.findByUsername(username)      // 예외처리 1. 작성자가 아닌 회원의 신청서 거절 요청
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 삭제된 게시글의 신청서 거절 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        Form form = formRepository.findById(formId)             // 예외처리 3. 존재하지 않는 신청서를 거절 요청
                .orElseThrow(() -> new IllegalArgumentException("신청폼이 존재하지 않습니다."));

        form.updateFormStatus(FormStatus.REFUSE);
    }
}
