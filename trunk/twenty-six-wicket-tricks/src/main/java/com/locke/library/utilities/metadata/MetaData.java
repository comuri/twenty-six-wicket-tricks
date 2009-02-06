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
package com.locke.library.utilities.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Typesafe map of metadata information
 * 
 * @author Jonathan Locke
 */
public class MetaData implements Serializable {

	private static final long serialVersionUID = -7051142109067684993L;

	private Map<Key<?>, Object> map = new HashMap<Key<?>, Object>();

	public static class Key<T> implements Serializable {

		private static final long serialVersionUID = 2117935572189337440L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			return obj != null && getClass().isInstance(obj);
		}
	}

	/**
	 * @param key
	 *            The metadata key
	 * @return True if the metadata exists
	 */
	public boolean contains(final Key<?> key) {
		return map.containsKey(key);
	}

	/**
	 * @param <T>
	 *            The type of the metadata object
	 * @param key
	 *            Key to the given metadata
	 * @param object
	 *            The new metadata object for the key
	 */
	public <T> void put(final Key<T> key, final T object) {
		map.put(key, object);
	}

	/**
	 * @param <T>
	 *            The type of the metadata object
	 * @param key
	 *            The key to the metadata
	 * @return The metadata object for the key
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final Key<T> key) {
		return (T) map.get(key);
	}
}