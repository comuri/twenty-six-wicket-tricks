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
package com.locke.library.persistence.dao.query.clauses;

import com.locke.library.persistence.dao.query.Clause;

/**
 * Clause for selecting a range of objects in the result
 * 
 * @author Jonathan Locke
 */
public class Range extends Clause {

	private static final long serialVersionUID = 592134343965489736L;

	private final long first;
	private final long count;

	public Range(long first, long count) {
		this.first = first;
		this.count = count;
	}

	public long getFirst() {
		return first;
	}

	public long getCount() {
		return count;
	}
}