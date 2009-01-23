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
package com.locke.library.web.behaviors.update;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import com.locke.library.event.IEventBroadcasterLocator;
import com.locke.library.web.events.AjaxUpdateEvent;

public class AjaxFormComponentUpdateBroadcastingBehavior extends
		AjaxFormComponentUpdatingBehavior {

	private static final long serialVersionUID = 5628920028467218468L;

	public AjaxFormComponentUpdateBroadcastingBehavior() {
		this("onchange");
	}

	public AjaxFormComponentUpdateBroadcastingBehavior(final String event) {
		super(event);
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		final Component component = getComponent();
		final IEventBroadcasterLocator locator = component
				.findParent(IEventBroadcasterLocator.class);
		if (locator != null) {
			locator.getEventBroadcaster().broadcast(
					new AjaxUpdateEvent(component, target));
		}
	}
}
