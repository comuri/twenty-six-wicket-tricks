package com.locke.library.persistence.dao.jpa;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.PersistentIdentifier;

public abstract class AbstractJpaDao<T extends IPersistent<PersistentIdentifier>>
		extends AbstractPrimaryKeyedJpaDao<T, PersistentIdentifier>
{
	public AbstractJpaDao(Class<T> type)
	{
		super(type);
	}
}
