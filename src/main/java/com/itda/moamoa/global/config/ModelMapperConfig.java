package com.itda.moamoa.global.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

//DTO와 Entity 간 변환을 위한 modelmapper
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .getConfiguration()
                .setFieldMatchingEnabled(true) // 필드 매칭 활성화
                .setFieldAccessLevel(AccessLevel.PRIVATE) // private 필드 접근 허용
                .setMatchingStrategy(MatchingStrategies.STRICT); // 엄격한 매칭 전략 사용. 다른 선택지 LOOSE

        return modelMapper;
    }
}
