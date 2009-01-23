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

import org.apache.wicket.util.time.Time;

/**
 * An event is an object which has a source and is sent (to a listener) at some
 * particular time.
 * 
 * @author Jonathan Locke
 */
public interface IEvent<T> {

	/**
	 * @return The source of the event
	 */
	T getSource();

	/**
	 * Called by event senders just before sending the event
	 */
	void sending();

	/**
	 * @return The time the event was sent
	 */
	Time getSentAt();
}
