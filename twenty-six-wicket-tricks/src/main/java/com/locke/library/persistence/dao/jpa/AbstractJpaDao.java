/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.library.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.wicket.util.lang.Classes;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.IPrimaryKey;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.clauses.Ascending;
import com.locke.library.persistence.dao.query.clauses.Count;
import com.locke.library.persistence.dao.query.clauses.Descending;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.persistence.dao.query.clauses.Range;

/**
 * Base class for JPA DAO implementations
 * 
 * @author Jonathan Locke
 * 
 * @param <T>
 */
public abstract class AbstractJpaDao<T extends IPersistent<PK>, PK extends IPrimaryKey>
{
	/**
	 * JPA entity manager injected by Spring
	 */
	private EntityManager entityManager;

	/**
	 * Class of object managed by this DAO
	 */
	private final Class<T> type;

	/**
	 * @param type
	 *            Type of object managed by this DAO
	 */
	public AbstractJpaDao(final Class<T> type)
	{
		this.type = type;
	}

	/**
	 * @param clauses
	 *            Zero or more clauses for locating matches. If no clauses are
	 *            given, all objects will match.
	 * @return The number of objects that matched the given clauses
	 */
	public long count(Clause... clauses)
	{

		// Add count clause before clauses passed in
		List<Clause> newClauses = new ArrayList<Clause>();
		newClauses.add(new Count());
		newClauses.addAll(Arrays.asList(clauses));

		// Build query
		final Query query = buildQuery(newClauses);

		// Result of query should be a count
		Long count = (Long) query.getSingleResult();
		if (count == null)
		{
			return 0;
		}
		return count;
	}

	/**
	 * @param object
	 *            Object to create in storage
	 */
	public void create(T object)
	{
		entityManager.persist(object);
	}

	/**
	 * @param object
	 *            Object to delete from storage
	 */
	public void delete(T object)
	{
		entityManager.remove(object);
	}

	/**
	 * Delete all objects of this type
	 */
	public void deleteAll()
	{
		entityManager.createQuery("delete from " + getName()).executeUpdate();
	}

	/**
	 * @param clauses
	 *            Zero or more clauses for locating matches. If no clauses are
	 *            given, all objects will match.
	 * @return The objects that matched the given clauses
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(Clause... clauses)
	{
		return (List<T>) buildQuery(Arrays.asList(clauses)).getResultList();
	}

	/**
	 * @param id
	 *            Id of object to read
	 * @return The loaded object
	 */
	public T read(PK id)
	{
		return (T) entityManager.find(type, id);
	}

	/**
	 * Spring will use this setter to inject a JPA Entity manager.
	 * 
	 * @param entityManager
	 *            Entity manager from Spring
	 */
	public void setEntityManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	/**
	 * @param object
	 *            The object to update
	 */
	public void update(T object)
	{
		entityManager.persist(object);
	}

	/**
	 * @param clauses
	 *            The list of abstract clauses to build a query for
	 * @return The query for the given clauses
	 */
	protected abstract Query buildQuery(List<Clause> clauses);

	/**
	 * @return The name of this DAO
	 */
	private String getName()
	{
		return Classes.simpleName(type);
	}

	/**
	 * Query builder for use in subclasses in implementing buildQuery().
	 * 
	 * @author Jonathan
	 */
	public abstract class AbstractJpaQueryBuilder
	{

		/**
		 * Abstracted clauses we're building a query for
		 */
		private final List<Clause> clauses;

		/**
		 * "EJBQL" query string
		 */
		private StringBuilder ejbql = new StringBuilder();

		/**
		 * @param clauses
		 *            Clauses
		 */
		public AbstractJpaQueryBuilder(List<Clause> clauses)
		{
			this.clauses = clauses;
		}

		/**
		 * @return Query
		 */
		@SuppressWarnings("unchecked")
		public Query build()
		{

			// Count clause included?
			Count count = getClause(Count.class);
			if (count != null)
			{
				append("select count(*) ");
			}

			// Always add this
			append("from " + getName() + " target where 1=1 ");

			// Add match constraints
			Match<T> match = getClause(Match.class);
			if (match != null)
			{
				if (!match.getObject().getClass().isAssignableFrom(type))
				{
					throw new IllegalArgumentException("Invalid match clause");
				}
				onMatch(match);
			}

			// Add sort ordering clauses
			Ascending ascending = getClause(Ascending.class);
			if (ascending != null)
			{
				onAscending(ascending);
			}
			Descending descending = getClause(Descending.class);
			if (descending != null)
			{
				onDescending(descending);
			}

			// Create query
			Query query = entityManager.createQuery(ejbql.toString());

			// Set range on query
			Range range = getClause(Range.class);
			if (range != null)
			{
				query.setFirstResult((int) range.getFirst());
				query.setMaxResults((int) range.getCount());
			}
			return query;
		}

		/**
		 * @param name
		 *            Name of property to match
		 * @param value
		 *            Value it should be
		 */
		protected void addMatchConstraint(String name, Object value)
		{
			if (value != null)
			{
				append("and upper(target." + name + ") like ('" + value + "')");
			}
		}

		protected void append(String string)
		{
			ejbql.append(string);
		}

		/**
		 * Override this method to provide multi-level sorting for a field
		 * 
		 * @param ascending
		 *            Order by clause
		 */
		protected void onAscending(Ascending ascending)
		{
			append("order by (target." + ascending.getField() + ") asc");
		}

		/**
		 * Override this method to provide multi-level sorting for a field
		 * 
		 * @param descending
		 *            Order by clause
		 */
		protected void onDescending(Descending descending)
		{
			append("order by (target." + descending.getField() + ") desc");
		}

		/**
		 * Adds HQL clauses for all fields of the match object that are
		 * populated with non-null values
		 * 
		 * @param match
		 *            The object to match by example
		 */
		protected abstract void onMatch(Match<T> match);

		/**
		 * Finds a given clause by type if it was passed in to the constructor
		 * 
		 * @param <C>
		 *            Clause type
		 * @param type
		 *            The type of clause desired
		 * @return The clause
		 */
		@SuppressWarnings("unchecked")
		private <C extends Clause> C getClause(Class<C> type)
		{
			for (Clause clause : clauses)
			{
				if (clause.getClass().equals(type))
				{
					return (C) clause;
				}
			}
			return null;
		}
	}
}
