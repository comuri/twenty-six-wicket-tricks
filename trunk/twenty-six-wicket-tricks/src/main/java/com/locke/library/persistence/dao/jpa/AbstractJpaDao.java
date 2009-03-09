/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.library.persistence.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.apache.wicket.util.lang.Classes;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.IDao;
import com.locke.library.persistence.dao.query.AbstractDaoQuery;
import com.locke.library.persistence.dao.query.Clause;
import com.locke.library.persistence.dao.query.clauses.Match;

/**
 * Base class for JPA DAO implementations
 * 
 * @author Jonathan Locke
 * 
 * @param <T>
 */
public abstract class AbstractJpaDao<T extends IPersistent<PK>, PK extends Serializable>
		implements IDao<T, PK>
{
	/**
	 * Class of object managed by this DAO
	 */
	final Class<T> type;

	/**
	 * @param type
	 *            Type of object managed by this DAO
	 */
	public AbstractJpaDao(final Class<T> type)
	{
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public void create(T object)
	{
		getEntityManager().persist(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T object)
	{
		getEntityManager().remove(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T ensure(T object)
	{
		T found = query(new Match<T>(object)).firstMatch();
		if (found != null)
		{
			return found;
		}
		create(object);
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	public <C extends Clause> AbstractDaoQuery<T, PK> query(C... clauses)
	{
		return new JpaQuery<T, PK>(this, clauses);
	}

	/**
	 * {@inheritDoc}
	 */
	public T read(PK id)
	{
		return (T) getEntityManager().find(type, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(T object)
	{
		getEntityManager().persist(object);
	}

	/**
	 * @return Entity manager to use
	 */
	protected abstract EntityManager getEntityManager();

	/**
	 * @return The name of this DAO
	 */
	String getName()
	{
		return Classes.simpleName(type);
	}
}