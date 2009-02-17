package com.locke.library.persistence.dao.jpa;

import javax.persistence.EntityManager;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.PersistentIdentifier;

public abstract class AbstractJpaDao<T extends IPersistent<PersistentIdentifier>>
		extends AbstractPrimaryKeyedJpaDao<T, PersistentIdentifier>
{
	public AbstractJpaDao(EntityManager entityManager, Class<T> type)
	{
		super(entityManager, type);
	}
}
