package com.demo.application.service.impl;

import com.demo.application.domain.Annexure;
import com.demo.application.repository.AnnexureRepository;
import com.demo.application.service.AnnexureService;
import com.demo.application.service.dto.AnnexureDTO;
import com.demo.application.service.mapper.AnnexureMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Annexure}.
 */
@Service
@Transactional
public class AnnexureServiceImpl implements AnnexureService {

    private final Logger log = LoggerFactory.getLogger(AnnexureServiceImpl.class);

    private final AnnexureRepository annexureRepository;

    private final AnnexureMapper annexureMapper;

    public AnnexureServiceImpl(AnnexureRepository annexureRepository, AnnexureMapper annexureMapper) {
        this.annexureRepository = annexureRepository;
        this.annexureMapper = annexureMapper;
    }

    @Override
    public AnnexureDTO save(AnnexureDTO annexureDTO) {
        log.debug("Request to save Annexure : {}", annexureDTO);
        Annexure annexure = annexureMapper.toEntity(annexureDTO);
        annexure = annexureRepository.save(annexure);
        return annexureMapper.toDto(annexure);
    }

    @Override
    public Optional<AnnexureDTO> partialUpdate(AnnexureDTO annexureDTO) {
        log.debug("Request to partially update Annexure : {}", annexureDTO);

        return annexureRepository
            .findById(annexureDTO.getId())
            .map(
                existingAnnexure -> {
                    annexureMapper.partialUpdate(existingAnnexure, annexureDTO);

                    return existingAnnexure;
                }
            )
            .map(annexureRepository::save)
            .map(annexureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnexureDTO> findAll() {
        log.debug("Request to get all Annexures");
        return annexureRepository.findAll().stream().map(annexureMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnnexureDTO> findOne(Long id) {
        log.debug("Request to get Annexure : {}", id);
        return annexureRepository.findById(id).map(annexureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Annexure : {}", id);
        annexureRepository.deleteById(id);
    }
}
