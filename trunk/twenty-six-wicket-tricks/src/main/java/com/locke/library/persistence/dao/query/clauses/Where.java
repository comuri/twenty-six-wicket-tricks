package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.dao.query.Clause;

public class Where extends Clause
{
	private final String ejbql;

	public Where(final String ejbql)
	{
		this.ejbql = ejbql;
	}

	@Override
	public String toString()
	{
		return ejbql;
	}
}
