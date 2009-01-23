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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import com.locke.library.web.events.AjaxUpdateEvent;
import com.locke.library.web.models.StringListModel;
import com.locke.tricks.e.EventPanel;

@SuppressWarnings("serial")
public class ChoicePanel extends EventPanel {

	private static final long serialVersionUID = -2622366171197427896L;

	public ChoicePanel(String id) {
		super(id);
		final Form<Void> form = new Form<Void>("form");
		final DropDownChoice<String> choice = new DropDownChoice<String>(
				"choice", new Model<String>(), new StringListModel(
						new AbstractReadOnlyModel<String>() {
							@Override
							public String getObject() {
								return getString("choices");
							}
						}));
		choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				getEventBus().broadcast(
						new AjaxChoiceUpdateEvent(ChoicePanel.this, target,
								choice.getModelObject()));
			}
		});
		// choice.add(new AjaxFormComponentUpdateBroadcastingBehavior());
		form.add(choice);
		add(form);
	}

	public static class AjaxChoiceUpdateEvent extends AjaxUpdateEvent {

		private static final long serialVersionUID = 5654480812267122319L;

		private final String choice;

		public AjaxChoiceUpdateEvent(Component source,
				AjaxRequestTarget target, final String choice) {
			super(source, target);
			this.choice = choice;
		}

		public String getChoice() {
			return choice;
		}
	}
}
