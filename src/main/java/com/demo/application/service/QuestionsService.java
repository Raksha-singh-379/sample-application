package com.demo.application.service;

import com.demo.application.service.dto.QuestionsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.demo.application.domain.Questions}.
 */
public interface QuestionsService {
    /**
     * Save a questions.
     *
     * @param questionsDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionsDTO save(QuestionsDTO questionsDTO);

    /**
     * Partially updates a questions.
     *
     * @param questionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionsDTO> partialUpdate(QuestionsDTO questionsDTO);

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    List<QuestionsDTO> findAll();

    /**
     * Get the "id" questions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionsDTO> findOne(Long id);

    /**
     * Delete the "id" questions.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
