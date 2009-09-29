/*****************************************************************/
/*                                                               */
/* (C) Copyright 2009 by Networks In Motion, Inc.                */
/*                                                               */
/* The information contained herein is confidential, proprietary */
/* to Networks In Motion, Inc., and considered a trade secret as */
/* defined in section 499C of the penal code of the State of     */
/* California. Use of this information by anyone other than      */
/* authorized employees of Networks In Motion is granted only    */
/* under a written non-disclosure agreement, expressly           */
/* prescribing the scope and manner of such use.                 */
/*                                                               */
/*****************************************************************/

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

    public JpaQueryResult(final JpaQuery<T, PK> jpaQuery, final int pageSize)
    {
        this.jpaQuery = jpaQuery;
        this.pageSize = pageSize;
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

    public void setColumn(final int column)
    {
        this.column = column;
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
