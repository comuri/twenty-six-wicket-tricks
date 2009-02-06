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
package com.locke.library.web.models;

import java.io.Serializable;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.dao.IDao;

/**
 * Base class for persistent models. Holds the object's class and either an id
 * for the object or a reference to any loaded object, or both. This permits the
 * object to be persistent or not, and if it is persistent it can be loaded or
 * not.
 * 
 * @author Jonathan Locke
 * 
 * @param <T>
 */
public abstract class AbstractDaoModel<T extends IPersistent<PK>, PK extends Serializable>
		implements IModel<T>
{

	private static final long serialVersionUID = -5196138177877713403L;

	/**
	 * Id of persistent object if it's been persisted
	 */
	private PK id;

	/**
	 * Reference to persistent object (if loaded during this request cycle and
	 * not yet detached OR if not yet persisted and id is null)
	 */
	private T object;

	/**
	 * @param id
	 *            Object id
	 */
	public AbstractDaoModel(PK id)
	{
		this.id = id;
		InjectorHolder.getInjector().inject(this);
	}

	/**
	 * @param object
	 *            The object
	 */
	public AbstractDaoModel(T object)
	{
		this.id = object.getId();
		this.object = object;
		InjectorHolder.getInjector().inject(this);
	}

	/**
	 * @param model
	 *            Model to copy
	 */
	public AbstractDaoModel(IModel<T> model)
	{
		// NOTE: We cannot use AbstractDaoModel(T) here because the model object
		// may not have been fully navigated yet if the DAO is lazy-loading
		// fields (which might leave some fields null)
		this(model.getObject().getId());
	}

	/**
	 * Delete object using DAO
	 */
	public void delete()
	{
		getDao().delete(getObject());
	}

	/**
	 * {@inheritDoc}
	 */
	public void detach()
	{

		// If there is an entity
		if (object != null)
		{

			// and it has a persistent id assigned
			if (object.getId() != null)
			{

				// save the id and null out the entity
				id = object.getId();
				object = null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public T getObject()
	{

		// If the entity isn't loaded
		if (object == null)
		{

			// but there is an id
			if (id != null)
			{

				// ask DAO to load it
				object = getDao().read(id);
			}
		}
		return object;
	}

	/**
	 * Create or Update this persistent model
	 */
	public void save()
	{
		T object = getObject();
		if (object.getId() == null)
		{
			getDao().create(object);
		} else
		{
			getDao().update(object);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setObject(T object)
	{
		throw new UnsupportedOperationException(getClass()
				+ " does not support setObject(T object)");
	}

	/**
	 * @return The DAO for this model
	 */
	protected abstract IDao<T, PK> getDao();
}
