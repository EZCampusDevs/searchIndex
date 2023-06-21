package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_term",
uniqueConstraints = @UniqueConstraint(columnNames = { "schoo_id", "real_term_id" }))
public class Term {

    @Id
    @Column(name = "term_id", nullable = false)
    private Integer termId;

    @Column(name = "real_term_id", nullable = false)
    private Integer realTermId;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;
    
    @Column(name = "term_description", length = 128)
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
