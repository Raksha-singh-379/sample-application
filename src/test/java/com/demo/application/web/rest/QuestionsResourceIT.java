package com.demo.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.application.IntegrationTest;
import com.demo.application.domain.Questions;
import com.demo.application.repository.QuestionsRepository;
import com.demo.application.service.dto.QuestionsDTO;
import com.demo.application.service.mapper.QuestionsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionsResourceIT {

    private static final String DEFAULT_FORM_ID = "AAAAAAAAAA";
    private static final String UPDATED_FORM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
            .formId(DEFAULT_FORM_ID)
            .type(DEFAULT_TYPE)
            .subType(DEFAULT_SUB_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return questions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createUpdatedEntity(EntityManager em) {
        Questions questions = new Questions()
            .formId(UPDATED_FORM_ID)
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .description(UPDATED_DESCRIPTION);
        return questions;
    }

    @BeforeEach
    public void initTest() {
        questions = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();
        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getFormId()).isEqualTo(DEFAULT_FORM_ID);
        assertThat(testQuestions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestions.getSubType()).isEqualTo(DEFAULT_SUB_TYPE);
        assertThat(testQuestions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createQuestionsWithExistingId() throws Exception {
        // Create the Questions with an existing ID
        questions.setId(1L);
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].formId").value(hasItem(DEFAULT_FORM_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].subType").value(hasItem(DEFAULT_SUB_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL_ID, questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.formId").value(DEFAULT_FORM_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.subType").value(DEFAULT_SUB_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findById(questions.getId()).get();
        // Disconnect from session so that the updates on updatedQuestions are not directly saved in db
        em.detach(updatedQuestions);
        updatedQuestions.formId(UPDATED_FORM_ID).type(UPDATED_TYPE).subType(UPDATED_SUB_TYPE).description(UPDATED_DESCRIPTION);
        QuestionsDTO questionsDTO = questionsMapper.toDto(updatedQuestions);

        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getFormId()).isEqualTo(UPDATED_FORM_ID);
        assertThat(testQuestions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestions.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testQuestions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions.formId(UPDATED_FORM_ID).subType(UPDATED_SUB_TYPE);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getFormId()).isEqualTo(UPDATED_FORM_ID);
        assertThat(testQuestions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestions.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testQuestions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions.formId(UPDATED_FORM_ID).type(UPDATED_TYPE).subType(UPDATED_SUB_TYPE).description(UPDATED_DESCRIPTION);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getFormId()).isEqualTo(UPDATED_FORM_ID);
        assertThat(testQuestions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestions.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testQuestions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Delete the questions
        restQuestionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, questions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
