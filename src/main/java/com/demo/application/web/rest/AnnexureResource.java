package com.demo.application.web.rest;

import com.demo.application.repository.AnnexureRepository;
import com.demo.application.service.AnnexureService;
import com.demo.application.service.dto.AnnexureDTO;
import com.demo.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.demo.application.domain.Annexure}.
 */
@RestController
@RequestMapping("/api")
public class AnnexureResource {

    private final Logger log = LoggerFactory.getLogger(AnnexureResource.class);

    private static final String ENTITY_NAME = "annexure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnexureService annexureService;

    private final AnnexureRepository annexureRepository;

    public AnnexureResource(AnnexureService annexureService, AnnexureRepository annexureRepository) {
        this.annexureService = annexureService;
        this.annexureRepository = annexureRepository;
    }

    /**
     * {@code POST  /annexures} : Create a new annexure.
     *
     * @param annexureDTO the annexureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annexureDTO, or with status {@code 400 (Bad Request)} if the annexure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annexures")
    public ResponseEntity<AnnexureDTO> createAnnexure(@RequestBody AnnexureDTO annexureDTO) throws URISyntaxException {
        log.debug("REST request to save Annexure : {}", annexureDTO);
        if (annexureDTO.getId() != null) {
            throw new BadRequestAlertException("A new annexure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnexureDTO result = annexureService.save(annexureDTO);
        return ResponseEntity
            .created(new URI("/api/annexures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annexures/:id} : Updates an existing annexure.
     *
     * @param id the id of the annexureDTO to save.
     * @param annexureDTO the annexureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexureDTO,
     * or with status {@code 400 (Bad Request)} if the annexureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annexureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annexures/{id}")
    public ResponseEntity<AnnexureDTO> updateAnnexure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnexureDTO annexureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Annexure : {}, {}", id, annexureDTO);
        if (annexureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnexureDTO result = annexureService.save(annexureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annexures/:id} : Partial updates given fields of an existing annexure, field will ignore if it is null
     *
     * @param id the id of the annexureDTO to save.
     * @param annexureDTO the annexureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexureDTO,
     * or with status {@code 400 (Bad Request)} if the annexureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the annexureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the annexureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annexures/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnnexureDTO> partialUpdateAnnexure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnexureDTO annexureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Annexure partially : {}, {}", id, annexureDTO);
        if (annexureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnexureDTO> result = annexureService.partialUpdate(annexureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annexureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /annexures} : get all the annexures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annexures in body.
     */
    @GetMapping("/annexures")
    public List<AnnexureDTO> getAllAnnexures() {
        log.debug("REST request to get all Annexures");
        return annexureService.findAll();
    }

    /**
     * {@code GET  /annexures/:id} : get the "id" annexure.
     *
     * @param id the id of the annexureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annexureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annexures/{id}")
    public ResponseEntity<AnnexureDTO> getAnnexure(@PathVariable Long id) {
        log.debug("REST request to get Annexure : {}", id);
        Optional<AnnexureDTO> annexureDTO = annexureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annexureDTO);
    }

    /**
     * {@code DELETE  /annexures/:id} : delete the "id" annexure.
     *
     * @param id the id of the annexureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annexures/{id}")
    public ResponseEntity<Void> deleteAnnexure(@PathVariable Long id) {
        log.debug("REST request to delete Annexure : {}", id);
        annexureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
