package com.demo.application.service.mapper;

import com.demo.application.domain.*;
import com.demo.application.service.dto.AnnexureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annexure} and its DTO {@link AnnexureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnexureMapper extends EntityMapper<AnnexureDTO, Annexure> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnnexureDTO toDtoId(Annexure annexure);
}
