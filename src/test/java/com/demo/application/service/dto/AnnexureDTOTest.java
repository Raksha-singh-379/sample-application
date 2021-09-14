package com.demo.application.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.demo.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnexureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnexureDTO.class);
        AnnexureDTO annexureDTO1 = new AnnexureDTO();
        annexureDTO1.setId(1L);
        AnnexureDTO annexureDTO2 = new AnnexureDTO();
        assertThat(annexureDTO1).isNotEqualTo(annexureDTO2);
        annexureDTO2.setId(annexureDTO1.getId());
        assertThat(annexureDTO1).isEqualTo(annexureDTO2);
        annexureDTO2.setId(2L);
        assertThat(annexureDTO1).isNotEqualTo(annexureDTO2);
        annexureDTO1.setId(null);
        assertThat(annexureDTO1).isNotEqualTo(annexureDTO2);
    }
}
