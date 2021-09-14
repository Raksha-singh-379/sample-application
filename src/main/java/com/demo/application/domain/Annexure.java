package com.demo.application.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Annexure.
 */
@Entity
@Table(name = "annexure")
public class Annexure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private Boolean answer;

    @Column(name = "comment")
    private String comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Annexure id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getAnswer() {
        return this.answer;
    }

    public Annexure answer(Boolean answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getComment() {
        return this.comment;
    }

    public Annexure comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annexure)) {
            return false;
        }
        return id != null && id.equals(((Annexure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Annexure{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
