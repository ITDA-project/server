package com.itda.moamoa.domain.payment.service;

import com.itda.moamoa.domain.notification.service.NotificationService;
import com.itda.moamoa.domain.participant.entity.Role;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.payment.dto.PaymentRefundRequest;
import com.itda.moamoa.domain.payment.dto.PaymentStatusResponseDto;
import com.itda.moamoa.domain.payment.dto.PaymentVerifyRequest;
import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.payment.repository.PaymentRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.repository.SessionRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.global.fcm.dto.NotificationRequestDTO;
import com.itda.moamoa.global.fcm.dto.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PortOneApiClient portOneApiClient;
    private final UserRepository userRepository;
    private final SomoimRepository somoimRepository;
    private final SessionRepository sessionRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;
    private final PostRepository postRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void verifyPayment(PaymentVerifyRequest request, String userId) {
        // 아임포트에서 결제 내역 조회
        Map<String, Object> paymentInfo = portOneApiClient.getPaymentInfo(request.impUid());

        int amount = (int) paymentInfo.get("amount");
        String merchantUid = (String) paymentInfo.get("merchant_uid");
        String impUid = (String) paymentInfo.get("imp_uid");

        // DB 결제 정보 검증
        Optional<Payment> optionalPayment = paymentRepository.findByMerchantUid(merchantUid);

        // 사용자 조회 - 이메일 형식인 경우 username으로 조회
        User user;
        if (userId.contains("@")) {
            // 이메일 형식의 username으로 조회
            user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        } else {
            try {
                // 숫자 형식의 ID로 조회 시도
                Long userIdLong = Long.parseLong(userId);
                user = userRepository.findById(userIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            } catch (NumberFormatException e) {
                // ID가 숫자 형식이 아닌 경우 username으로 조회
                user = userRepository.findByUsername(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            }
        }

        if (optionalPayment.isPresent()) {
            // 기존 결제 정보가 있는 경우
            Payment payment = optionalPayment.get();

            if (!payment.getUser().getId().toString().equals(user.getId().toString())) {
                throw new SecurityException("본인 결제가 아님");
            }

            if (payment.getAmount() != amount) {
                throw new IllegalArgumentException("결제 금액 불일치");
            }

            // 결제 상태 갱신
            payment.markPaid();
            paymentRepository.save(payment);

            // 결제 완료 알림
            notifyHost(payment.getSomoim(), user, false);

        } else {
            // 결제 정보가 없는 경우, 새로 생성
            Somoim somoim = null;
            Session session = null;

            if (request.somoimId() != null) {
                somoim = somoimRepository.findById(request.somoimId())
                        .orElse(null);
            }

            if (request.sessionId() != null) {
                session = sessionRepository.findById(request.sessionId())
                        .orElse(null);
            }

            // 새 결제 정보 생성
            Payment newPayment = Payment.builder()
                    .merchantUid(merchantUid)
                    .impUid(impUid)
                    .user(user)
                    .somoim(somoim)
                    .session(session)
                    .amount(amount)
                    .status(Payment.PaymentStatus.PAID)
                    .username(user.getUsername())
                    .build();

            newPayment.markPaid();
            paymentRepository.save(newPayment);

            // 결제 완료 알림
            notifyHost(somoim, user, false);
        }
    }

    @Transactional
    public void refundPayment(PaymentRefundRequest request, String userId) {
        Payment payment = paymentRepository.findByImpUid(request.impUid())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        User user;
        if (userId.contains("@")) {
            // 이메일 형식의 username으로 조회
            user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        } else {
            try {
                // 숫자 형식의 ID로 조회 시도
                Long userIdLong = Long.parseLong(userId);
                user = userRepository.findById(userIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            } catch (NumberFormatException e) {
                // ID가 숫자 형식이 아닌 경우 username으로 조회
                user = userRepository.findByUsername(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            }
        }

        if (!payment.getUser().getId().toString().equals(user.getId().toString())) {
            throw new SecurityException("본인 결제만 환불 가능");
        }

        portOneApiClient.requestRefund(request.impUid(), request.amount());

        payment.markCancelled();
        paymentRepository.save(payment);

        // 소모임
        Somoim somoim = payment.getSession().getSomoim();

        // 환불 완료 알림
        notifyRefund(somoim, user, payment, true);
    }

    @Transactional(readOnly = true)
    public PaymentStatusResponseDto getPaymentStatus(Long roomId, List<Long> userIds) {
        // 1. 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));

        // 2. 채팅방 → 게시글 조회
        Post post = postRepository.findAll().stream()
                .filter(p -> p.getChatRoom() != null && p.getChatRoom().getId().equals(roomId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("채팅방에 연결된 게시글이 없습니다."));

        // 3. 게시글 → 소모임 조회
        Participant organizer = participantRepository.findByPostAndRole(post, Role.ORGANIZER)
                .orElseThrow(() -> new EntityNotFoundException("소모임 주최자 정보를 찾을 수 없습니다."));

        Somoim somoim = organizer.getSomoim();

        // 4. 현재 진행 중인 세션 조회
        Session activeSession = sessionRepository.findBySomoimAndStatus(somoim, Session.SessionStatus.IN_PROGRESS)
                .orElseThrow(() -> new EntityNotFoundException("진행 중인 세션이 없습니다."));

        // 5. 사용자 정보 조회
        List<User> users = userRepository.findAllById(userIds);

        // 6. 결제 정보 조회 (sessionId와 userIds로 직접 조회)
        List<Payment> paidPayments = paymentRepository.findPaidPaymentsBySessionAndUsers(activeSession.getId(), userIds);
        Map<Long, Payment> paymentMap = paidPayments.stream()
                .collect(Collectors.toMap(p -> p.getUser().getId(), p -> p));

        // 7. 응답 데이터 구성
        List<PaymentStatusResponseDto.UserPaymentStatus> userPaymentStatuses = users.stream()
                .map(user -> {
                    Payment payment = paymentMap.get(user.getId());
                    boolean isPaid = payment != null;
                    int paymentAmount = isPaid ? payment.getAmount() : 0;

                    return new PaymentStatusResponseDto.UserPaymentStatus(
                            user.getId(),
                            user.getUsername(),
                            user.getName(),
                            isPaid,
                            paymentAmount
                    );
                })
                .collect(Collectors.toList());

        return new PaymentStatusResponseDto(activeSession.getId(), userPaymentStatuses);
    }
    private void notifyHost(Somoim somoim, User payer, boolean isRefund) {
        if (somoim == null || payer == null) return;

        // 주최자
        User host = participantRepository.findBySomoimAndRole(somoim, Role.ORGANIZER);
        // 주최자가 생성한 소모임 직전에 게시한 게시글
        Post post = postRepository.findTopByUserAndCreatedAtBeforeOrderByCreatedAtDesc(host, somoim.getCreatedAt());

        // 결제 완료 알림
        if (!isRefund && host != null && post != null) {
            notificationService.saveAndSendNotification(
                    new NotificationRequestDTO(
                            host.getId(),
                            post.getTitle(),
                            payer.getName() + "님의 결제가 완료되었습니다.",
                            NotificationType.PAYMENT_COMPLETED,
                            null,
                            null
                    )
            );
        }
    }

    private void notifyRefund(Somoim somoim, User payer, Payment payment, boolean isRefund) {
        if (somoim == null || payer == null) return;

        // 주최자
        User host = participantRepository.findBySomoimAndRole(somoim, Role.ORGANIZER);
        // 주최자가 생성한 소모임 직전에 게시한 게시글
        Post post = postRepository.findTopByUserAndCreatedAtBeforeOrderByCreatedAtDesc(host, somoim.getCreatedAt());

        // 환불 완료 알림
        if (isRefund && post != null) {
            notificationService.saveAndSendNotification(
                    new NotificationRequestDTO(
                            payer.getId(),
                            post.getTitle(),
                            payment.getSession().getLocation() + "에서 진행된" + payment.getSession().getSessionNumber()+ "번째 모임의 환불이 완료되었습니다.",
                            NotificationType.REFUND_COMPLETED,
                            null,
                            null
                    )
            );
        }
    }
}