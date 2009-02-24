package com.locke.library.persistence.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.locke.library.persistence.IPersistent;

public abstract class AbstractJpaDao<T extends IPersistent<PK>, PK extends Serializable>
		extends AbstractPrimaryKeyedJpaDao<T, PK>
{
	public AbstractJpaDao(EntityManager entityManager, Class<T> type)
	{
		super(entityManager, type);
	}
}
