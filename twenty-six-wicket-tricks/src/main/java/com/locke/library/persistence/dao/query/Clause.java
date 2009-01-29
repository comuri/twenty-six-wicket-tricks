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
package com.locke.library.persistence.dao.query;

import com.locke.library.persistence.dao.Dao;
import com.locke.library.persistence.dao.query.clauses.Ascending;
import com.locke.library.persistence.dao.query.clauses.Descending;
import com.locke.library.persistence.dao.query.clauses.Match;
import com.locke.library.persistence.dao.query.clauses.Range;

/**
 * Base class for abstracted (DAO implementation independent) query clauses.
 * <p>
 * The idea here is that {@link Dao} interfaces can accept a set of abstracted
 * query clauses that can be translated into native queries by the DAO
 * implementation.
 * 
 * @see Ascending
 * @see Descending
 * @see Match
 * @see Range
 * 
 * @author Jonathan Locke
 */
public abstract class Clause {

	private static final long serialVersionUID = 592134343965489736L;

	public Clause() {
	}
}
