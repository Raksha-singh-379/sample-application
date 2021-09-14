package com.demo.application.service;

import com.demo.application.service.dto.AnnexureDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.demo.application.domain.Annexure}.
 */
public interface AnnexureService {
    /**
     * Save a annexure.
     *
     * @param annexureDTO the entity to save.
     * @return the persisted entity.
     */
    AnnexureDTO save(AnnexureDTO annexureDTO);

    /**
     * Partially updates a annexure.
     *
     * @param annexureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnnexureDTO> partialUpdate(AnnexureDTO annexureDTO);

    /**
     * Get all the annexures.
     *
     * @return the list of entities.
     */
    List<AnnexureDTO> findAll();

    /**
     * Get the "id" annexure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnexureDTO> findOne(Long id);

    /**
     * Delete the "id" annexure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
