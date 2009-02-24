package com.locke.library.persistence.dao.jpa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.query.AbstractDaoQuery;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.clauses.Ascending;
import com.locke.library.persistence.dao.query.clauses.Count;
import com.locke.library.persistence.dao.query.clauses.Descending;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.persistence.dao.query.clauses.Range;
import com.locke.library.persistence.dao.query.clauses.Where;

/**
 * Query builder for use in subclasses in implementing buildQuery().
 * 
 * @author Jonathan
 */
public class JpaQuery<T extends IPersistent<PK>, PK extends Serializable>
		extends AbstractDaoQuery<T, PK>
{
	/**
	 * Abstracted clauses we're building a query for
	 */
	private final List<? extends Clause> clauses;

	/**
	 * "EJBQL" query string
	 */
	private StringBuilder ejbql = new StringBuilder();

	/**
	 * The DAO that this query is for
	 */
	private final AbstractPrimaryKeyedJpaDao<T, PK> dao;

	/**
	 * @param clauses
	 *            Clauses
	 */
	public JpaQuery(AbstractPrimaryKeyedJpaDao<T, PK> dao, Clause[] clauses)
	{
		this(dao, Arrays.asList(clauses));
	}

	/**
	 * @param clauses
	 *            Clauses
	 */
	public JpaQuery(AbstractPrimaryKeyedJpaDao<T, PK> dao, List<? extends Clause> clauses)
	{
		this.clauses = clauses;
		this.dao = dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countMatches()
	{
		// Add count clause before clauses passed in
		List<Clause> newClauses = new ArrayList<Clause>();
		newClauses.add(new Count());
		newClauses.addAll(clauses);

		// Result of query should be a count
		Long count = (Long) build(newClauses).getSingleResult();
		if (count == null)
		{
			return 0;
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete()
	{
		dao.entityManager.createQuery(
				"delete from " + dao.getName() + " where ").executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T firstMatch()
	{
		List<T> found = matches();
		if (found != null && found.size() > 0)
		{
			return found.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> matches()
	{
		return (List<T>) build(clauses).getResultList();
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
			final BeanInfo info = Introspector.getBeanInfo(object.getClass());

			// Go through properties
			for (PropertyDescriptor property : info.getPropertyDescriptors())
			{
				addMatchConstraint(property.getName(), property.getReadMethod()
						.invoke(object));
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

	@SuppressWarnings("unchecked")
	private Query build(List<? extends Clause> clauses)
	{
		// Count clause included?
		final Count count = getClause(Count.class);
		if (count != null)
		{
			append("select count(*) ");
		}

		// Always add this
		append("from " + dao.getName() + " target where 1=1 ");

		// Add match constraints
		final Match<T> match = getClause(Match.class);
		if (match != null)
		{
			if (!match.getObject().getClass().isAssignableFrom(dao.type))
			{
				throw new IllegalArgumentException("Invalid match clause");
			}
			onMatch(match);
		}

		// Add where constraints if no match clause
		final Where where = getClause(Where.class);
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
		final Ascending ascending = getClause(Ascending.class);
		if (ascending != null)
		{
			onAscending(ascending);
		}
		final Descending descending = getClause(Descending.class);
		if (descending != null)
		{
			onDescending(descending);
		}

		// Create query
		final Query query = dao.entityManager.createQuery(ejbql.toString());

		// Set range on query
		final Range range = getClause(Range.class);
		if (range != null)
		{
			query.setFirstResult((int) range.getFirst());
			query.setMaxResults((int) range.getCount());
		}
		return query;
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