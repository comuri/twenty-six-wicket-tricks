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
package com.locke.library.persistence.dao;

import java.util.List;

import com.locke.library.persistence.IPersistent;
import com.locke.library.persistence.IPrimaryKey;
import com.locke.library.persistence.dao.query.Clause;

/**
 * Interface to CRUD (Create, Read, Update, Delete) functionality which persists
 * objects that are {@link IPersistent} as well as find and count functionality
 * which permits (abstracted) queries using {@link Clause}s which filter, sort
 * and limit results.
 * 
 * @author Jonathan Locke
 * 
 * @param <T>
 */
public interface IDao<T extends IPersistent<PK>, PK extends IPrimaryKey>
{
	/**
	 * CREATE object
	 * 
	 * @param object
	 *            Object to create
	 */
	void create(T object);

	/**
	 * READ from data source
	 * 
	 * @param id
	 *            Persistent id
	 * @return Loaded object
	 */
	T read(PK id);

	/**
	 * UPDATE object
	 * 
	 * @param object
	 *            Object to update
	 */
	void update(T object);

	/**
	 * DELETE object
	 * 
	 * @param object
	 *            Object to delete
	 */
	void delete(T object);

	/**
	 * DELETE objects matching clauses
	 * 
	 * @param clauses
	 *            The clauses
	 */
	void delete(Clause... clauses);

	/**
	 * DELETE all objects of this type
	 */
	void deleteAll();

	/**
	 * @param clauses
	 *            Clauses to use in query
	 * @return All objects in the given range
	 */
	List<T> find(Clause... clauses);

	/**
	 * @param clauses
	 *            Clauses to use in query
	 * @return Number of objects matching the example object
	 */
	long count(Clause... clauses);
}
