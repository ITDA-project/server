package com.itda.moamoa.domain.form.service;

import com.itda.moamoa.domain.form.dto.FormItemDTO;
import com.itda.moamoa.domain.form.dto.FormListResponseDTO;
import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.entity.FormStatus;
import com.itda.moamoa.domain.form.repository.FormRepository;
import com.itda.moamoa.domain.notification.service.NotificationService;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.domain.participant.entity.*;
import com.itda.moamoa.global.fcm.dto.NotificationRequestDTO;
import com.itda.moamoa.global.fcm.dto.NotificationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;

    // 커서 기반 페이지네이션으로 폼 목록 조회
    public FormListResponseDTO getFormsByCursor(long postId, Long cursor, int size, String username) {
        // 사용자 권한 검증
        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
        
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        
        // 게시글 작성자인지 확인
        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("신청서를 열람할 권한이 없습니다.");
        }
        
        // 초기 요청인 경우 가장 큰 ID 값 설정
        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        
        // 페이지 사이즈 설정
        PageRequest pageRequest = PageRequest.of(0, size);
        
        // 커서 기반으로 id가 작은 순서대로 폼 목록 조회 (FormStatus.APPLY인 것만)
        List<Form> forms = formRepository.findByPostAndFormStatusAndFormIdLessThanOrderByFormIdDesc(post, FormStatus.APPLY, cursor, pageRequest);
        
        // FormItemDTO 리스트로 변환
        List<FormItemDTO> formItemDTOs = forms.stream()
                .map(form -> FormItemDTO.builder()
                        .formId(form.getFormId())
                        .userName(form.getUser().getName())
                        .userImage(form.getUser().getImage())
                        .build())
                .collect(Collectors.toList());
        
        // FormListResponseDTO 생성 및 반환
        return FormListResponseDTO.builder()
                .postTitle(post.getTitle())
                .forms(formItemDTOs)
                .build();
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    public FormResponseDTO getFormById(long postId, long formId, String username) {
        // 예외처리 1. 존재하지 않는 사용자의 신청서 조회 요청 확인
        userRepository.findByUsername(username)
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

        // 알림 전송
        notificationService.saveAndSendNotification(
                new NotificationRequestDTO(
                    post.getUser().getId(),
                    post.getTitle(),
                    "새로운 신청자가 있어요. 확인해보세요!",
                    NotificationType.FORM_APPLY,
                    null,
                    null
                )
        );
    }

    // 신청서 수락
    @Transactional
    public void acceptForm(long postId, long formId, String username) {
        // 예외처리 1. 존재하지 않는 사용자의 신청서 수락 요청 확인
        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
                
        Post post = postRepository.findById(postId)             // 예외처리 2. 삭제된 게시글의 신청서 수락 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!post.getUser().getUsername().equals(username))     // 예외처리 3. 권한이 없는 사용자(Not host)의 신청서 수락 요청
            throw new IllegalStateException("신청서를 수락할 권한이 없습니다.");

        Form form = formRepository.findById(formId)             // 예외처리 3. 존재하지 않는 신청서를 수락 요청
                .orElseThrow(() -> new IllegalArgumentException("신청폼이 존재하지 않습니다."));

        form.updateFormStatus(FormStatus.ACCEPT);
        
        // organizer의 participant 정보에서 somoim 정보 가져오기
        Participant organizerParticipant = participantRepository.findByPostAndRole(post, Role.ORGANIZER)
                .orElseThrow(() -> new IllegalArgumentException("게시글 작성자 정보를 찾을 수 없습니다."));
        
        // 폼 작성자를 participant로 추가 (somoim 정보 포함)
        User formCreator = form.getUser();
        Participant formCreatorParticipant = Participant.builder()
                .user(formCreator)
                .post(post)
                .somoim(organizerParticipant.getSomoim())  // organizer의 somoim 정보 사용
                .role(Role.PARTICIPANT)
                .participantStatus(ParticipantStatus.ENTER)
                .build();
        
        participantRepository.save(formCreatorParticipant);
        
        // 참가자 수 증가
        post.incrementParticipantCount();
        postRepository.save(post);

        // 알림 전송
        notificationService.saveAndSendNotification(
                new NotificationRequestDTO(
                        form.getUser().getId(),
                        post.getTitle(),
                        "모임 신청이 수락 되었습니다. 함께 모임에 참여해요!",
                        NotificationType.FORM_APPROVED,
                        null,
                        formCreatorParticipant.getSomoim().getId()
                )
        );
    }

    // 신청서 거절
    @Transactional
    public void refuseForm(long postId, long formId, String username) {
        // 예외처리 1. 존재하지 않는 사용자의 신청서 거절 요청 확인
        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));
                
        Post post = postRepository.findById(postId)             // 예외처리 2. 삭제된 게시글의 신청서 거절 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!post.getUser().getUsername().equals(username))     // 예외처리 3. 권한이 없는 사용자(Not host)의 신청서 거절 요청
            throw new IllegalStateException("신청서를 거절할 권한이 없습니다.");

        Form form = formRepository.findById(formId)             // 예외처리 3. 존재하지 않는 신청서를 거절 요청
                .orElseThrow(() -> new IllegalArgumentException("신청폼이 존재하지 않습니다."));

        form.updateFormStatus(FormStatus.REFUSE);

        // 알림 전송
        notificationService.saveAndSendNotification(
                new NotificationRequestDTO(
                        form.getUser().getId(),
                        post.getTitle(),
                        "모임 신청이 거절 되었습니다. 다른 모임에 신청해보세요!",
                        NotificationType.FORM_REJECTED,
                        post.getPostId(),
                        null
                )
        );
    }
}
