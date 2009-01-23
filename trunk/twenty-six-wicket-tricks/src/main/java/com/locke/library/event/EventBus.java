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
package com.locke.library.event;

import java.io.Serializable;
import java.util.Set;

import com.locke.library.utilities.collections.MapSet;

/**
 * Implements a listener/broadcaster event bus.
 * 
 * @author Jonathan Locke
 */
public class EventBus implements IEventBroadcaster, Serializable {

	private static final long serialVersionUID = -560616216159145553L;

	MapSet<Class<?>, IEventListener<IEvent<?>>> listeners = new MapSet<Class<?>, IEventListener<IEvent<?>>>();

	@SuppressWarnings("unchecked")
	public <T extends IEvent<?>> void addListener(Class<T> eventType,
			IEventListener<T> listener) {
		listeners.add(eventType, (IEventListener<IEvent<?>>) listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends IEvent<?>> void removeListener(
			IEventListener<T> listener, Class<T> eventType) {
		listeners.remove(eventType, (IEventListener<IEvent<?>>) listener);
	}

	public <T extends IEvent<?>> boolean broadcast(T event) {
		Set<IEventListener<IEvent<?>>> set = listeners.get(event.getClass());
		boolean sent = false;
		if (set != null) {
			for (IEventListener<IEvent<?>> listener : set) {
				event.sending();
				listener.onEvent(event);
				sent = true;
			}
		}
		return sent;
	}
}
