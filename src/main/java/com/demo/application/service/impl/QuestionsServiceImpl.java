package com.demo.application.service.impl;

import com.demo.application.domain.Questions;
import com.demo.application.repository.QuestionsRepository;
import com.demo.application.service.QuestionsService;
import com.demo.application.service.dto.QuestionsDTO;
import com.demo.application.service.mapper.QuestionsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questions}.
 */
@Service
@Transactional
public class QuestionsServiceImpl implements QuestionsService {

    private final Logger log = LoggerFactory.getLogger(QuestionsServiceImpl.class);

    private final QuestionsRepository questionsRepository;

    private final QuestionsMapper questionsMapper;

    public QuestionsServiceImpl(QuestionsRepository questionsRepository, QuestionsMapper questionsMapper) {
        this.questionsRepository = questionsRepository;
        this.questionsMapper = questionsMapper;
    }

    @Override
    public QuestionsDTO save(QuestionsDTO questionsDTO) {
        log.debug("Request to save Questions : {}", questionsDTO);
        Questions questions = questionsMapper.toEntity(questionsDTO);
        questions = questionsRepository.save(questions);
        return questionsMapper.toDto(questions);
    }

    @Override
    public Optional<QuestionsDTO> partialUpdate(QuestionsDTO questionsDTO) {
        log.debug("Request to partially update Questions : {}", questionsDTO);

        return questionsRepository
            .findById(questionsDTO.getId())
            .map(
                existingQuestions -> {
                    questionsMapper.partialUpdate(existingQuestions, questionsDTO);

                    return existingQuestions;
                }
            )
            .map(questionsRepository::save)
            .map(questionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionsDTO> findAll() {
        log.debug("Request to get all Questions");
        return questionsRepository.findAll().stream().map(questionsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionsDTO> findOne(Long id) {
        log.debug("Request to get Questions : {}", id);
        return questionsRepository.findById(id).map(questionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questions : {}", id);
        questionsRepository.deleteById(id);
    }
}
