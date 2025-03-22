package com.itda.moamoa.domain.form.service;

import com.itda.moamoa.domain.form.repository.FormRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service        // Service Annotation
public class FormService {
    @Autowired  // 미리 생성된 객체 사용 Annotation
    private FormRepository formRepository;

    // 신청폼 조회

    // 신청폼 생성
    @Transactional  // Transation Annotation
    public
    // 신청폼 수정
    @Transactional

    // 신청폼 삭제
    @Transactional
}
