package com.demo.application.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.demo.application.domain.Questions} entity.
 */
public class QuestionsDTO implements Serializable {

    private Long id;

    private String formId;

    private String type;

    private String subType;

    private String description;

    private AnnexureDTO annexure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AnnexureDTO getAnnexure() {
        return annexure;
    }

    public void setAnnexure(AnnexureDTO annexure) {
        this.annexure = annexure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionsDTO)) {
            return false;
        }

        QuestionsDTO questionsDTO = (QuestionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionsDTO{" +
            "id=" + getId() +
            ", formId='" + getFormId() + "'" +
            ", type='" + getType() + "'" +
            ", subType='" + getSubType() + "'" +
            ", description='" + getDescription() + "'" +
            ", annexure=" + getAnnexure() +
            "}";
    }
}
