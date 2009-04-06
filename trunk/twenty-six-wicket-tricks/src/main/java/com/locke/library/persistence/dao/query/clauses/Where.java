package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.QueryText;

public class Where extends Clause
{
    private final QueryText text = new QueryText();

    public Where(final String ejbql)
    {
        this.text.add(ejbql);
    }

    public void add(String ejbql)
    {
        this.text.add(ejbql);
    }

    @Override
    public String toString()
    {
        return this.text.toString();
    }
}
