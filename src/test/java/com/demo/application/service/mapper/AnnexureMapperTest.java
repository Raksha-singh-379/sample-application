package com.demo.application.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnexureMapperTest {

    private AnnexureMapper annexureMapper;

    @BeforeEach
    public void setUp() {
        annexureMapper = new AnnexureMapperImpl();
    }
}
