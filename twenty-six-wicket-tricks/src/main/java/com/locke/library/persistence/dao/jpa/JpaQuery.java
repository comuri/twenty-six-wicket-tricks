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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.wicket.util.string.StringList;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.IQuery;
import com.locke.library.persistence.dao.query.QueryText;
import com.locke.library.persistence.dao.query.clauses.Ascending;
import com.locke.library.persistence.dao.query.clauses.Count;
import com.locke.library.persistence.dao.query.clauses.Descending;
import com.locke.library.persistence.dao.query.clauses.Distinct;
import com.locke.library.persistence.dao.query.clauses.Fetch;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.persistence.dao.query.clauses.Range;
import com.locke.library.persistence.dao.query.clauses.Where;
import com.locke.library.utilities.object.Type;
import com.locke.library.utilities.strings.MethodName;

/**
 * Query builder for use in subclasses in implementing buildQuery().
 * 
 * @author Jonathan
 */
public class JpaQuery<T extends IPersistent<PK>, PK extends Serializable> implements IQuery<T>
{
    Class<T> restrictToType;

    private boolean addedMatchConstraint;

    /**
     * Abstracted clauses we're building a query for
     */
    private final ClauseList clauses;

    /**
     * The DAO that this query is for
     */
    private final AbstractJpaDao<T, PK> dao;

    private boolean queryablePropertyFound;

    /**
     * The EJBQL text we're building
     */
    private final QueryText queryText = new QueryText();

    /**
     * @param clauses
     *            Clauses
     */
    public JpaQuery(final AbstractJpaDao<T, PK> dao, final ClauseList clauses)
    {
        this.clauses = clauses;
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public int countMatches()
    {
        // Add count clause before clauses passed in
        final ClauseList newClauses = new ClauseList();
        newClauses.add(new Count());
        newClauses.addAll(this.clauses);

        // Result of query should be a count
        final Long count = (Long)build(newClauses).getSingleResult();
        if (count == null)
        {
            return 0;
        }
        return count.intValue();
    }

    /**
     * {@inheritDoc}
     */
    public void delete()
    {
        this.queryText.add("delete");
        build().executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public T firstMatch()
    {
        for (final T match : matches())
        {
            return match;
        }
        return null;
    }

    public Iterable<T> matches()
    {
        return page(Integer.MAX_VALUE);
    }

    public Iterable<T> page(final int pageSize)
    {
        return new JpaQueryResult<T, PK>(this, pageSize)
        {
            @Override
            protected void onBeforeNextPage()
            {
                getEntityManager().close();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        build(this.clauses);
        return this.queryText.toString();
    }

    /**
     * @param name
     *            Name of property to match
     * @param value
     *            Value it should be
     */
    protected void addMatchConstraint(final String name, final Object value)
    {
        this.addedMatchConstraint = true;
        if (value instanceof String || value instanceof Character)
        {
            // All single quote must be replaced with two single quotes to avoid
            // confusing the DB EJBQL compiler. The interpreter will treat the
            // back-to-back single quotes as a lone single quote
            // TODO: There should be an object that ensures that the format is
            // valid, not just handling this one case.
            this.queryText.and("target." + name + " = '" + value.toString().replaceAll("'", "''")
                               + "'");
        }
        else if (value instanceof Number || value instanceof Boolean)
        {
            this.queryText.and("target." + name + " = " + value);
        }
        else
        {
            throw new UnsupportedOperationException(
                                                    "Cannot add match constraint for value of class "
                                                            + value.getClass());
        }
    }

    /**
     * Adds match constraints for all fields of the match object that are
     * populated with non-null values
     * 
     * @param match
     *            The object to match by example
     */
    protected void addMatchConstraints(final String baseName, final Object object)
    {
        try
        {
            final Type type = Type.forClass(object.getClass());
            for (final Method method : type.annotatedMethods(Queryable.class))
            {
                this.queryablePropertyFound = true;

                // Get the return type and invoke the method to get the
                // actual return value
                final Class<?> returnType = method.getReturnType();
                method.setAccessible(true);
                final Object returnValue = method.invoke(object);
                if (returnValue != null)
                {
                    // Get the name of the method, prefixed with any
                    // base name
                    String name = new MethodName(method).getName();
                    if (baseName != null)
                    {
                        name = baseName + "." + name;
                    }

                    // If the return value is a supported type
                    if (isSupported(returnType))
                    {
                        // Add a match constraint for that value
                        addMatchConstraint(name, returnValue);
                    }
                    else
                    {
                        // Add match constraints for sub-object
                        addMatchConstraints(name, returnValue);
                    }
                }
            }
        }
        catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (final IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (final InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Override this method to provide multi-level sorting for a field
     * 
     * @param ascending
     *            Order by clause
     */
    protected void onAscending(final Ascending ascending)
    {
        final StringList fields = new StringList();
        for (final String field : ascending.getFields())
        {
            fields.add("target." + field);
        }
        this.queryText.add("order by " + fields.join() + " asc");
    }

    /**
     * Override this method to provide multi-level sorting for a field
     * 
     * @param descending
     *            Order by clause
     */
    protected void onDescending(final Descending descending)
    {
        final StringList fields = new StringList();
        for (final String field : descending.getFields())
        {
            fields.add("target." + field);
        }
        this.queryText.add("order by " + fields.join() + " desc");
    }

    /**
     * Adds match constraints for all fields of the match object that are
     * populated with non-null values
     * 
     * @param match
     *            The object to match by example
     */
    protected void onMatch(final Match<T> match)
    {
        this.addedMatchConstraint = false;
        addMatchConstraints(null, match.getObject());
        if (!this.addedMatchConstraint)
        {
            if (!this.queryablePropertyFound)
            {
                throw new IllegalStateException("No @Queryable properties found in match against "
                                                + match.getObject().getClass());
            }
            else
            {
                throw new IllegalStateException("All @Queryable properties found in match against "
                                                + match.getObject().getClass() + " were null");
            }
        }
    }

    Query build()
    {
        return build(this.clauses);
    }

    @SuppressWarnings("unchecked")
    private Query build(final ClauseList clauses)
    {
        this.queryText.clear();

        // Count clause included?
        final Count count = clauses.find(Count.class);
        if (count != null)
        {
            this.queryText.add("select count(*)");
        }

        for (final Distinct distinct : clauses.findAll(Distinct.class))
        {
            this.queryText.add("select distinct target, target." + distinct.getField());
        }

        // Always add this
        this.queryText.add("from " + this.dao.getName() + " as target");

        // Add any fetch clauses
        for (final Fetch fetch : clauses.findAll(Fetch.class))
        {
            this.queryText.add("left join fetch target." + fetch.getField() + " as ignored"
                               + fetch.getField());
        }

        this.queryText.add("where 1=1");

        // Add match constraints
        final Match<T> match = clauses.find(Match.class);
        if (match != null)
        {
            if (!match.getObject().getClass().isAssignableFrom(this.dao.type))
            {
                throw new IllegalArgumentException("Invalid match clause: " + match);
            }
            onMatch(match);
        }

        // Add where constraints if no match clause
        for (final Where where : clauses.findAll(Where.class))
        {
            this.queryText.and("(" + where + ")");
        }

        // Add sort ordering clauses
        final Ascending ascending = clauses.find(Ascending.class);
        if (ascending != null)
        {
            onAscending(ascending);
        }
        final Descending descending = clauses.find(Descending.class);
        if (descending != null)
        {
            onDescending(descending);
        }

        // Create query
        final EntityManager entityManager = getEntityManager();
        final Query query = entityManager.createQuery(this.queryText.toString());

        // Set range on query
        final Range range = clauses.find(Range.class);
        if (range != null)
        {
            query.setFirstResult((int)range.getFirst());
            query.setMaxResults((int)range.getCount());
        }
        return query;
    }

    private EntityManager getEntityManager()
    {
        return this.dao.getEntityManager();
    }

    private boolean isSupported(final Class<?> returnType)
    {
        return returnType.isPrimitive() || Number.class.isAssignableFrom(returnType)
               || String.class.isAssignableFrom(returnType)
               || Character.class.isAssignableFrom(returnType)
               || Boolean.class.isAssignableFrom(returnType);
    }
}
