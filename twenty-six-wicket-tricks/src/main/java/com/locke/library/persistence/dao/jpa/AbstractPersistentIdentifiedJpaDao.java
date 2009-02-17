package com.locke.library.persistence.dao.jpa;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.PersistentIdentifier;

public abstract class AbstractPersistentIdentifiedJpaDao<T extends IPersistent<PersistentIdentifier>>
		extends AbstractJpaDao<T, PersistentIdentifier>
{
	public AbstractPersistentIdentifiedJpaDao(Class<T> type)
	{
		super(type);
	}
}
