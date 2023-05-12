package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_word_course_data")
public class WordMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    //1 word_id from `Word` to many here
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "count")
    private int count;

    
    @ManyToOne
    @JoinColumn(name = "course_data_id")
    private CourseData courseDataId;

    public WordMap() {}
    
    public WordMap(Word word, int count, CourseData courseDataId) {
        this.word = word;
        this.count = count;
        this.courseDataId = courseDataId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Word getWord() {
        return word;
    }
    
    public CourseData getCourseData() {
    	return courseDataId;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
