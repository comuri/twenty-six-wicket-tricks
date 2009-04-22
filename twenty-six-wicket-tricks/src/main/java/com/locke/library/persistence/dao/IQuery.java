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

package com.locke.library.persistence.dao;

/**
 * @author jlocke
 */
public interface IQuery<T>
{
    /**
     * @return The query text (for debugging)
     */
    public abstract String queryString();

    /**
     * @return Number of objects matching this query
     */
    int countMatches();

    /**
     * Delete all objects matching this query
     */
    void delete();

    /**
     * @return First matching object
     */
    T firstMatch();

    /**
     * @return All matching objects
     */
    Iterable<T> matches();

    /**
     * @return Pages through matching objects with the given page size. The
     *         session may be closed at the end of each page.
     */
    Iterable<T> page(int pageSize);
}
