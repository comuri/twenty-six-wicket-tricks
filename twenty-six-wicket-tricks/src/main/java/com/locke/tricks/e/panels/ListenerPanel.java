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
package com.locke.tricks.e.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.locke.library.event.IEventListener;
import com.locke.tricks.e.EventPanel;
import com.locke.tricks.e.panels.ChoicePanel.AjaxChoiceUpdateEvent;
import com.locke.tricks.e.panels.events.ClickEvent;

@SuppressWarnings("serial")
public class ListenerPanel extends EventPanel implements
		IEventListener<ClickEvent> {

	private String message = "Waiting...";
	private boolean addedListeners = false;

	public ListenerPanel(String id) {
		super(id);
		add(new Label("message", new AbstractReadOnlyModel<String>() {

			@Override
			public String getObject() {
				return message;
			}
		}));
		setOutputMarkupId(true);
	}

	public void onEvent(ClickEvent event) {
		message = "Received: " + event;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// We cannot do this in the constructor because the panel is not yet
		// attached to a page at that time and the event bus is in the page
		if (!addedListeners) {
			getEventBus().addListener(ClickEvent.class, this);
			getEventBus().addListener(AjaxChoiceUpdateEvent.class,
					new IEventListener<AjaxChoiceUpdateEvent>() {
						public void onEvent(final AjaxChoiceUpdateEvent event) {
							message = "Received: " + event + ", choice was "
									+ event.getChoice();
							event.getTarget().addComponent(ListenerPanel.this);
						}
					});
			addedListeners = true;
		}
	}
}
