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

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.locke.library.persistence.dao.query.QueryText;

/**
 * @author jlocke
 */
public abstract class JpaQueryResult<T> implements Iterable<T>, Iterator<T>
{
    private int index = 0;
    private final int pageSize;
    private List<T> results;

    public JpaQueryResult(final int pageSize)
    {
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

    /**
     * @return The built query
     */
    protected abstract Query buildQuery();

    protected abstract QueryText queryText();

    @SuppressWarnings("unchecked")
    private void fetchPage()
    {
        // we try to find more results
        final Query query = buildQuery();
        query.setFirstResult(this.index);
        query.setMaxResults(this.pageSize);
        try
        {
            this.results = query.getResultList();
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Failed executing query " + queryText(), e);
        }
    }

    private int pageIndex()
    {
        return this.index % this.pageSize;
    }
}
