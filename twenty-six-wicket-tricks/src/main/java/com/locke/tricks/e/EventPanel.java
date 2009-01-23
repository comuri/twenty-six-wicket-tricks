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
package com.locke.tricks.e;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.locke.library.event.EventBus;
import com.locke.library.event.EventSender;
import com.locke.library.event.IEventBroadcaster;
import com.locke.library.event.IEventListener;
import com.locke.library.web.locators.ComponentMatcher;

public class EventPanel extends Panel {

	private static final long serialVersionUID = 5867392815332239409L;

	public EventPanel(String id) {
		super(id);
	}

	public EventPanel(String id, IModel<?> model) {
		super(id, model);
	}

	public EventBus getEventBus() {
		return findParent(EventPage.class).getEventBus();
	}

	public IEventBroadcaster getPageEventBroadcaster() {
		return new EventSender<Component>(new ComponentMatcher(getPage(),
				IEventListener.class));
	}
}
