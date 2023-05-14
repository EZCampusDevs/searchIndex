package org.ezcampus.search.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_word_course_data")

@NamedQueries({
	@NamedQuery(name = "insertWordMap", query = "INSERT INTO WordMap (word, courseData, count) "
            + "SELECT :word, :courseData, "
            + "COALESCE((SELECT wm.count + :count FROM WordMap wm WHERE wm.word = :word AND wm.courseData = :courseData), :count)")
})
public class WordMap {
    
    //1 word_id from `Word` to many here
    
    @Id
    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @Id
    @ManyToOne
    @JoinColumn(name = "course_data_id")
    private CourseData courseData;

    @Column(name = "count")
    private int count;

    
    public WordMap() {}

    public WordMap(Word word, CourseData courseDataId, int count) {
        this.word = word;
        this.count = count;
        this.courseData = courseDataId;
    }


    public Word getWord() {
        return word;
    }
    
    public CourseData getCourseData() {
    	return courseData;
    }

    public void setWord(Word word) {
        this.word = word;
    }
    
    public void setCourseData(CourseData cd) {
    	this.courseData = cd;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseCountBy(int inc) {
    	this.count += inc;
    }
}
