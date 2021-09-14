package com.demo.application.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.demo.application.domain.Annexure} entity.
 */
public class AnnexureDTO implements Serializable {

    private Long id;

    private Boolean answer;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnexureDTO)) {
            return false;
        }

        AnnexureDTO annexureDTO = (AnnexureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, annexureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnexureDTO{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
