package com.itda.moamoa.global.security.jwt.repository;

import com.itda.moamoa.global.security.jwt.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

//refresh 토큰에 대한 repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    //제너릭 - Refresh 엔티티, id

    Boolean existsByRefresh(String refresh);

    @Transactional //값 변경하므로
    void deleteByRefresh(String refresh);

}
