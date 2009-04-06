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
public class JpaQueryResult<T> extends AbstractQueryResult<T> implements Iterable<T>
{
    private int index;
    private final int pageSize;
    private final Query query;
    private List<T> results;

    public JpaQueryResult(final Query query, final int pageSize)
    {
        this.query = query;
        this.pageSize = pageSize;
        this.index = 0;
        query.setFirstResult(0);
        query.setMaxResults(pageSize);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            /**
             * {@inheritDoc}
             */
            public boolean hasNext()
            {
                page();
                return JpaQueryResult.this.index < JpaQueryResult.this.results.size();
            }

            /**
             * {@inheritDoc}
             */
            public T next()
            {
                page();
                return JpaQueryResult.this.results.get(JpaQueryResult.this.index++);
            }

            /**
             * {@inheritDoc}
             */
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> matches()
    {
        return this;
    }

    @SuppressWarnings("unchecked")
    private void page()
    {
        // If there are no more results
        if (this.index >= this.results.size())
        {
            // and the results list was filled
            if (this.results.size() == this.pageSize)
            {
                // try to find more results
                this.query.setFirstResult(this.index + this.pageSize);
                // TODO set query hint for cursor
                this.results = this.query.getResultList();
            }
        }
    }
}
