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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.locke.library.persistence.IPersistent;

/**
 * @author jlocke
 */
public abstract class JpaQueryResult<T extends IPersistent<PK>, PK extends Serializable>
                                                                                         implements
                                                                                         Iterable<T>,
                                                                                         Iterator<T>
{
    private int column = 0;
    private int index = 0;
    private final JpaQuery<T, PK> jpaQuery;
    private final int pageSize;
    private List<T> results;

    public JpaQueryResult(final JpaQuery<T, PK> jpaQuery, final int pageSize, final int column)
    {
        this.jpaQuery = jpaQuery;
        this.pageSize = pageSize;
        this.column = column;
        fetchPage();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        return pageIndex() < this.results.size();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<T> iterator()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<T> matches()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public T next()
    {
        // Get next result
        final int pageIndex = pageIndex();
        if (pageIndex == 0 && this.index != 0)
        {
            onBeforeNextPage();
            fetchPage();
        }
        final T result = this.results.get(pageIndex);
        this.index++;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    protected abstract void onBeforeNextPage();

    @SuppressWarnings("unchecked")
    private void fetchPage()
    {
        // we try to find more results
        final Query query = this.jpaQuery.build();

        query.setFirstResult(this.index);
        query.setMaxResults(this.pageSize);
        try
        {
            final List<?> results = query.getResultList();
            if (!results.isEmpty() && results.get(0).getClass().isArray())
            {
                final List<T> firstColumn = new ArrayList<T>();
                for (final Object object : results)
                {
                    firstColumn.add((T)((Object[])object)[this.column]);
                }
                this.results = firstColumn;
            }
            else
            {
                this.results = (List<T>)results;
            }
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Failed executing jpaQuery " + this.jpaQuery, e);
        }
    }

    private int pageIndex()
    {
        return this.index % this.pageSize;
    }
}
