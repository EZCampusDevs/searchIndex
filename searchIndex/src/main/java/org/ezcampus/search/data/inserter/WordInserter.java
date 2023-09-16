package org.ezcampus.search.data.inserter;

import org.ezcampus.search.hibernate.entity.CourseData;
import org.hibernate.Session;

public interface WordInserter
{
	public void init(Session session);
	
	public void insertWord(Session session, String value);
	
	public void associateWords(Session session, CourseData courseData);
}
