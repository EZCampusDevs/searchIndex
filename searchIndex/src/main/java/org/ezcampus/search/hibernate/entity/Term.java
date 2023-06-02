package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_term")
public class Term {

    @Id
    @Column(name = "term_id", nullable = false)
    private Integer termId;


    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;
    
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
