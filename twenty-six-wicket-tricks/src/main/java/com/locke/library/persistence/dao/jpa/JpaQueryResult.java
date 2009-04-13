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

import com.locke.library.persistence.dao.query.AbstractQueryResult;

/**
 * @author jlocke
 */
public abstract class JpaQueryResult<T> extends AbstractQueryResult<T> implements Iterable<T>, Iterator<T>
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
        return this.index % this.pageSize < this.results.size();
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
    @Override
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
        final int resultsIndex = this.index % this.pageSize;
        final T result = this.results.get(resultsIndex);
        this.index++;

        // If we're at the end of this page
        if (resultsIndex == this.results.size() - 1)
        {
            fetchPage();
        }
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

    @SuppressWarnings("unchecked")
    private void fetchPage()
    {
        // we try to find more results
        final Query query = buildQuery();
        query.setFirstResult(this.index);
        query.setMaxResults(this.pageSize);
        this.results = query.getResultList();
    }
}
