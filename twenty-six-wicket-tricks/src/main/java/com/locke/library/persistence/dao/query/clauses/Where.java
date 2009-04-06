package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.QueryText;

public class Where extends Clause
{
    private final QueryText text;

    public Where(final QueryText text)
    {
        this.text = text;
    }

    public Where(final String ejbql)
    {
        this.text = new QueryText();
        this.text.add(ejbql);
    }

    public void add(String ejbql)
    {
        this.text.add(ejbql);
    }

    public void and(String ejbql)
    {
        this.text.and(ejbql);
    }

    @Override
    public String toString()
    {
        return this.text.toString();
    }
}
