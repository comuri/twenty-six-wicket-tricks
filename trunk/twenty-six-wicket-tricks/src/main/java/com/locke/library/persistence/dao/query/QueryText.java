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

package com.locke.library.persistence.dao.query;

/**
 * Holds text being built up for a query language statement or clause.
 * 
 * @author jlocke
 */
public class QueryText
{
    private final StringBuilder text = new StringBuilder();

    public void add(String string)
    {
        if (this.text.length() > 0)
        {
            append(" ");
        }
        append(string);
    }

    public void append(String string)
    {
        this.text.append(string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return this.text.toString();
    }
}
