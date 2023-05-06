package org.ezcampus.search.hibernate.entity;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_word",indexes = {@Index(columnList = "word", name = "word_index")})
public class Word
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "word_id")
	private int wordId;

    @NaturalId
	@Column(name = "word", unique=true)
	private String word;

	public Word()
	{

	}

	public Word(String word)
	{
		this.word = word;
	}

	public int getId()
	{
		return wordId;
	}

	public void setId(int id)
	{
		this.wordId = id;
	}

	public String getFirstName()
	{
		return word;
	}

	public void setFirstName(String word)
	{
		this.word = word;
	}

	public String toString()
	{
		return String.format("[%d %s]", this.wordId, this.word);
	}
}
