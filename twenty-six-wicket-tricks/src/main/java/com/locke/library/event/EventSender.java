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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.locke.library.locator.ILocator;

/**
 * Class for sending events to a specific target or to a list of targets located
 * by an implementation of {@link ILocator}.
 * <p>
 * NOTE: The down-side of {@link EventSender} (as opposed to {@link EventBus})
 * is that located target objects can only implement one event listener (of any
 * one single class of events) due to limitations in generics (you cannot
 * implement IEvent<X> and IEvent<Y> on the same class even though both have a
 * distinct method signature).
 * 
 * @author Jonathan Locke
 */
public class EventSender<T> implements IEventBroadcaster {

	private final ILocator<T> targetLocator;

	/**
	 * Construct.
	 */
	public EventSender() {
		this.targetLocator = null;
	}

	/**
	 * Construct.
	 * 
	 * @param targetLocator
	 *            The target locator
	 */
	public EventSender(ILocator<T> targetLocator) {
		this.targetLocator = targetLocator;
	}

	/**
	 * @param targetLocator
	 *            The target locator
	 * @param event
	 *            The event to send to all located targets
	 * @return True if the event was sent to at least one target
	 */
	public <E extends IEvent<?>> boolean broadcast(E event) {
		boolean sent = false;
		for (T target : targetLocator.all()) {
			sent = send(target, event) || sent;
		}
		return sent;
	}

	/**
	 * @param target
	 *            The listener to send to
	 * @param event
	 *            The event to send
	 * @return True if the event was sent
	 */
	public boolean send(T target, IEvent<?> event) {
		try {
			final Method method = target.getClass().getMethod("onEvent",
					event.getClass());
			if (method != null) {
				event.sending();
				try {
					method.invoke(target, new Object[] { event });
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return true;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
		}
		return false;
	}
}
