package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_term")
public class Term {

    @Id
    @Column(name = "term_id")
    private Integer termId;

    @Column(name = "term_description")
    private String termDescription;

    public Term() {}

    public Term(Integer termId, String termDescription) {
        this.termId = termId;
        this.termDescription = termDescription;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTermDescription() {
        return termDescription;
    }

    public void setTermDescription(String termDescription) {
        this.termDescription = termDescription;
    }
}
