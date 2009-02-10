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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
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
import com.locke.library.persistence.dao.query.clauses.Where;

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
	 * Query builder for use in subclasses in implementing buildQuery().
	 * 
	 * @author Jonathan
	 */
	public class AbstractJpaQueryBuilder
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

			// Add where constraints if no match clause
			Where where = getClause(Where.class);
			if (where != null)
			{
				if (match != null)
				{
					throw new IllegalStateException(
							"Cannot use match and where clauses together");
				}
				append("and (" + where + ")");
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
			if (value instanceof String)
			{
				append("and upper(target." + name + ") like ('" + value + "')");
			}
			throw new UnsupportedOperationException(
					"Cannot add match constraint for value of class "
							+ value.getClass());
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
		 * Adds match constraints for all fields of the match object that are
		 * populated with non-null values
		 * 
		 * @param match
		 *            The object to match by example
		 */
		protected void onMatch(Match<T> match)
		{
			T object = match.getObject();
			try
			{
				// Get properties
				final BeanInfo info = Introspector.getBeanInfo(object
						.getClass());

				// Go through properties
				for (PropertyDescriptor property : info
						.getPropertyDescriptors())
				{
					addMatchConstraint(property.getName(), property
							.getReadMethod().invoke(object));
				}
			}
			catch (IntrospectionException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}

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

	/**
	 * @param type
	 *            Type of object managed by this DAO
	 */
	public AbstractJpaDao(final Class<T> type)
	{
		this.type = type;
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	public void create(T object)
	{
		entityManager.persist(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T object)
	{
		entityManager.remove(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(Clause... clauses)
	{
		return (List<T>) buildQuery(Arrays.asList(clauses)).getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	public T findFirst(Clause... clauses)
	{
		List<T> found = find(clauses);
		if (found != null && found.size() > 0)
		{
			return found.get(0);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T findOrCreate(T object)
	{
		T found = findFirst(new Match<T>(object));
		if (found != null)
		{
			return found;
		}
		create(object);
		return object;
	}
	
	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	protected Query buildQuery(List<Clause> clauses)
	{
		return new AbstractJpaQueryBuilder(clauses).build();
	}

	/**
	 * @return The name of this DAO
	 */
	private String getName()
	{
		return Classes.simpleName(type);
	}
}
