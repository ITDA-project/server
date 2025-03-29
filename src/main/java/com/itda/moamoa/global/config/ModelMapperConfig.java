package com.itda.moamoa.global.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

//DTO와 Entity 간 변환 자동화 modelmapper
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .getConfiguration()
                .setFieldMatchingEnabled(true)                      // 필드 매칭 활성화 - Setter 불필요
                .setFieldAccessLevel(AccessLevel.PRIVATE)           // private 필드 접근 허용
                .setMatchingStrategy(MatchingStrategies.STRICT);    // 필드명이 완벽히 일치해야 Mapping

        return modelMapper;
    }
}
