package com.demo.application.service.mapper;

import com.demo.application.domain.*;
import com.demo.application.service.dto.QuestionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Questions} and its DTO {@link QuestionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { AnnexureMapper.class })
public interface QuestionsMapper extends EntityMapper<QuestionsDTO, Questions> {
    @Mapping(target = "annexure", source = "annexure", qualifiedByName = "id")
    QuestionsDTO toDto(Questions s);
}
