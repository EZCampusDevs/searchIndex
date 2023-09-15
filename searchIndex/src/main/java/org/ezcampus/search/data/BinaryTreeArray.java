package org.ezcampus.search.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.ezcampus.search.hibernate.entity.Word;

public class BinaryTreeArray extends ArrayList<Word>
{
	
	public Word get(String o) 
	{
		int index = Collections.binarySearch(this, o);
		
		if(index < 0) 
		{
			return null;
		}
		
		return super.get(index);
	}

	public boolean contains(String o) 
	{
		return Collections.binarySearch(this, ((String)o)) >= 0;
	}
	
	public boolean contains(Word o) 
	{
		return Collections.binarySearch(this, ((Word)o).getWordString()) >= 0;
	}

	@Override
	public boolean contains(Object o)
	{

		if(o instanceof Word) {
			return this.contains(((Word)o).getWordString());
		}

		if(o instanceof String) {
			return this.contains(((String)o));
		}
		
		return false;
	}

	@Override
	public boolean add(Word e)
	{
		int index = Collections.binarySearch(this, e.getWordString());
		
		if(index < 0) 
		{
			super.add(-index - 1, e);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean remove(Object o)
	{
		if(! (o instanceof Word))
			return false;
		
		int index = Collections.binarySearch(this, ((Word)o).getWordString());
		
		if(index < 0)
			return false;
		
		return super.remove(index) != null;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return c.stream().allMatch(this::contains);
	}

	@Override
	public boolean addAll(Collection<? extends Word> c)
	{
		int size = this.size();
		
		super.ensureCapacity(this.size() + c.size());
		
		c.forEach(this::add);
		
		return this.size() != size;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Word> c)
	{
		return this.addAll(c);
	}

	@Override
	public int indexOf(Object o)
	{
		if(o instanceof Word) 
		{
			int i = Collections.binarySearch(this, ((Word)o).getWordString());
			
			if(i >= 0)
				return i;
		}
		
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.indexOf(o);
	}
}
