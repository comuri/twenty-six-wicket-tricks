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

import java.util.ArrayList;
import java.util.List;

import com.locke.library.persistence.dao.query.Clause;

/**
 * @author jlocke
 */
public class ClauseList extends ArrayList<Clause>
{
    private static final long serialVersionUID = -6862554357767675069L;

    public ClauseList()
    {
    }

    public ClauseList(final Clause... clauses)
    {
        for (final Clause clause : clauses)
        {
            add(clause);
        }
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
    public <C extends Clause> C find(final Class<C> type)
    {
        final List<C> found = findAll(type);
        if (found.size() == 0)
        {
            return null;
        }
        if (found.size() > 1)
        {
            throw new IllegalStateException("More than one clause of type " + type + " found");
        }
        return found.get(0);
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
    public <C extends Clause> List<C> findAll(final Class<C> type)
    {
        final List<C> list = new ArrayList<C>();
        for (final Clause clause : this)
        {
            if (clause.getClass().equals(type))
            {
                list.add((C)clause);
            }
        }
        return list;
    }
}
