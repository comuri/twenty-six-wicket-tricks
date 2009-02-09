package com.locke.library.persistence.dao;

import java.util.List;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.clauses.Match;

public class DaoExpander<T extends IPersistent<?>>
{
	private final IDao<T, ?> dao;

	public DaoExpander(IDao<T, ?> dao)
	{
		this.dao = dao;
	}
	
	public T findFirst(Clause... clauses) {
		List<T> found = dao.find(clauses);
		if (found != null && found.size() > 0) {
			return found.get(0);
		}
		return null;
	}
	
	public T findOrCreate(T object) {
		T found = findFirst(new Match<T>(object));
		if (found != null) {
			return found;
		}
		dao.create(object);
		return object;
	}
}
