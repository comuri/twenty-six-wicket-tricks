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
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.query.AbstractDaoQuery;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.QueryText;
import com.locke.library.persistence.dao.query.clauses.Ascending;
import com.locke.library.persistence.dao.query.clauses.Count;
import com.locke.library.persistence.dao.query.clauses.Descending;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.persistence.dao.query.clauses.Range;
import com.locke.library.persistence.dao.query.clauses.Where;
import com.locke.library.utilities.strings.MethodName;

/**
 * Query builder for use in subclasses in implementing buildQuery().
 * 
 * @author Jonathan
 */
public class JpaQuery<T extends IPersistent<PK>, PK extends Serializable> extends
                                                                          AbstractDaoQuery<T, PK>
{
    private boolean addedMatchConstraint;

    /**
     * Abstracted clauses we're building a query for
     */
    private final List<? extends Clause> clauses;

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
    public JpaQuery(AbstractJpaDao<T, PK> dao, Clause[] clauses)
    {
        this(dao, Arrays.asList(clauses));
    }

    /**
     * @param clauses
     *            Clauses
     */
    public JpaQuery(AbstractJpaDao<T, PK> dao, List<? extends Clause> clauses)
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
        newClauses.addAll(this.clauses);

        // Result of query should be a count
        Long count = (Long)build(newClauses).getSingleResult();
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
        this.queryText.add("delete");
        build(this.clauses).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T firstMatch()
    {
        Iterator<T> found = matches();
        if (found != null && found.hasNext())
        {
            return found.next();
        }
        return null;
    }

    @Override
    public Iterator<T> matches()
    {
        return new JpaQueryResult<T>(build(this.clauses), 1000);
    }

    /**
     * @param name
     *            Name of property to match
     * @param value
     *            Value it should be
     */
    protected void addMatchConstraint(String name, Object value)
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
    protected void addMatchConstraints(String baseName, Object object)
    {
        try
        {
            // Go through methods on this object
            for (Method method : object.getClass().getMethods())
            {
                // Go through annotations on the method
                for (Annotation annotation : method.getAnnotations())
                {
                    // If the method is queryable
                    if (annotation instanceof Queryable)
                    {
                        this.queryablePropertyFound = true;

                        // Get the return type and invoke the method to get the
                        // actual return value
                        final Class<?> returnType = method.getReturnType();
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
            }
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
     * Override this method to provide multi-level sorting for a field
     * 
     * @param ascending
     *            Order by clause
     */
    protected void onAscending(Ascending ascending)
    {
        this.queryText.add("order by (target." + ascending.getField() + ") asc");
    }

    /**
     * Override this method to provide multi-level sorting for a field
     * 
     * @param descending
     *            Order by clause
     */
    protected void onDescending(Descending descending)
    {
        this.queryText.add("order by (target." + descending.getField() + ") desc");
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

    @SuppressWarnings("unchecked")
    private Query build(List<? extends Clause> clauses)
    {
        // Count clause included?
        final Count count = getClause(Count.class);
        if (count != null)
        {
            this.queryText.add("select count(*)");
        }

        // Always add this
        this.queryText.add("from " + this.dao.getName() + " target where 1=1");

        // Add match constraints
        final Match<T> match = getClause(Match.class);
        if (match != null)
        {
            if (!match.getObject().getClass().isAssignableFrom(this.dao.type))
            {
                throw new IllegalArgumentException("Invalid match clause: " + match);
            }
            onMatch(match);
        }

        // Add where constraints if no match clause
        final Where where = getClause(Where.class);
        if (where != null)
        {
            if (match != null)
            {
                throw new IllegalStateException("Cannot use match and where clauses together");
            }
            this.queryText.and("(" + where + ")");
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
        final Query query = this.dao.getEntityManager().createQuery(this.queryText.toString());

        // Set range on query
        final Range range = getClause(Range.class);
        if (range != null)
        {
            query.setFirstResult((int)range.getFirst());
            query.setMaxResults((int)range.getCount());
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
        for (Clause clause : this.clauses)
        {
            if (clause.getClass().equals(type))
            {
                return (C)clause;
            }
        }
        return null;
    }

    private boolean isSupported(final Class<?> returnType)
    {
        return returnType.isPrimitive() || Number.class.isAssignableFrom(returnType)
               || String.class.isAssignableFrom(returnType)
               || Character.class.isAssignableFrom(returnType)
               || Boolean.class.isAssignableFrom(returnType);
    }
}
