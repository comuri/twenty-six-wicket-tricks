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
package com.locke.library.utilities.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Map from a given key to a list of values
 * 
 * @author Jonathan Locke
 */
public class MapList<K, V> extends HashMap<K, List<V>> {

	private static final long serialVersionUID = 3462998132471897564L;

	public void add(K key, V value) {
		List<V> values = get(key);
		if (values == null) {
			values = new ArrayList<V>();
			put(key, values);
		}
		values.add(value);
	}

	public void remove(K key, V value) {
		List<V> values = get(key);
		if (values != null) {
			values.remove(value);
		}
	}
	
	public List<V> list(K key) {
		return super.get(key);
	}
	
	public V first(K key) {
		List<V> list = list(key);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
