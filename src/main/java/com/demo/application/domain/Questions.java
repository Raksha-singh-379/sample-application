package com.demo.application.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Questions.
 */
@Entity
@Table(name = "questions")
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "type")
    private String type;

    @Column(name = "sub_type")
    private String subType;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private Annexure annexure;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Questions id(Long id) {
        this.id = id;
        return this;
    }

    public String getFormId() {
        return this.formId;
    }

    public Questions formId(String formId) {
        this.formId = formId;
        return this;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getType() {
        return this.type;
    }

    public Questions type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return this.subType;
    }

    public Questions subType(String subType) {
        this.subType = subType;
        return this;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDescription() {
        return this.description;
    }

    public Questions description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Annexure getAnnexure() {
        return this.annexure;
    }

    public Questions annexure(Annexure annexure) {
        this.setAnnexure(annexure);
        return this;
    }

    public void setAnnexure(Annexure annexure) {
        this.annexure = annexure;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questions)) {
            return false;
        }
        return id != null && id.equals(((Questions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questions{" +
            "id=" + getId() +
            ", formId='" + getFormId() + "'" +
            ", type='" + getType() + "'" +
            ", subType='" + getSubType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
