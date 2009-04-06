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
    private final int pageSize;
    private final Query query;

    public JpaQueryResult(final Query query, final int pageSize)
    {
        this.query = query;
        this.pageSize = pageSize;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private int index = 0;
            private List<T> results;

            // Initializer block
            {
                JpaQueryResult.this.query.setFirstResult(0);
                JpaQueryResult.this.query.setMaxResults(JpaQueryResult.this.pageSize);
            }

            /**
             * {@inheritDoc}
             */
            public boolean hasNext()
            {
                page();
                return this.index < this.results.size();
            }

            /**
             * {@inheritDoc}
             */
            public T next()
            {
                page();
                return this.results.get(this.index++);
            }

            /**
             * {@inheritDoc}
             */
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unchecked")
            private void page()
            {
                // If no results, or we're on the first index of a page
                if (this.results == null || this.index % JpaQueryResult.this.pageSize == 0)
                {
                    // we try to find more results
                    JpaQueryResult.this.query.setFirstResult(this.index);
                    // TODO set query hint for cursor... maybe pre-fetch on
                    // separate thread if cursoring doesn't pre-fetch?
                    this.results = JpaQueryResult.this.query.getResultList();
                }
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
}
