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

package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.dao.query.Clause;

/**
 * @author jlocke
 */
public class Fetch extends Clause
{
    private final String field;

    public Fetch(final String field)
    {
        this.field = field;
    }

    public String getField()
    {
        return this.field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return this.field;
    }
}
