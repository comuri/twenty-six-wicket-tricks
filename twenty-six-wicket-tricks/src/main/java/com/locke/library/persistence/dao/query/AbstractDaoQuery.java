package com.locke.library.persistence.dao.query;

import java.util.List;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.IPrimaryKey;

public abstract class AbstractDaoQuery<T extends IPersistent<PK>, PK extends IPrimaryKey>
{
	/**
	 * @return First matching object
	 */
	public abstract T firstMatch();

	/**
	 * @param clauses
	 *            Clauses to use in query
	 * @return All matching objects
	 */
	public abstract List<T>  matches();
	
	/**
	 * @return Number of objects matching this query
	 */
	public abstract long countMatches();

	/**
	 * Delete all objects matching this query
	 */
	public abstract void delete();
}
