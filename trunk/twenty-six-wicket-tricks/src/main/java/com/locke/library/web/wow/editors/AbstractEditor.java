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
package com.locke.library.web.wow.editors;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.locke.library.web.generics.GenericPanel;
import com.locke.library.web.panels.IPanelFactory;
import com.locke.library.web.panels.feedback.LocalFeedbackContainer;
import com.locke.library.web.panels.feedback.LocalFeedbackPanel;
import com.locke.library.web.wow.layouts.ColumnLayout;
import com.locke.library.web.wow.layouts.IComponentSource;

public abstract class AbstractEditor<T> extends Panel {

	private static final long serialVersionUID = 8090922830584929662L;

	public AbstractEditor(String id, IModel<T> model,
			IComponentSource<Panel> panels) {
		super(id);
		final LocalFeedbackContainer feedbackContainer = new LocalFeedbackContainer(
				"feedbackContainer");
		GenericPanel<T> form = newFormPanel("formPanel", model,
				getFields(panels));
		feedbackContainer.add(form);
		add(feedbackContainer);
		add(newFeedbackPanel(feedbackContainer));
	}

	private LocalFeedbackPanel newFeedbackPanel(
			final LocalFeedbackContainer feedbackContainer) {
		return new LocalFeedbackPanel("feedback", feedbackContainer);
	}

	protected IPanelFactory getFields(final IComponentSource<Panel> panels) {
		return new IPanelFactory() {
			public Panel newPanel(String id) {
				return new ColumnLayout(id, panels, 2);
			}
		};
	}

	protected abstract AbstractFormPanel<T> newFormPanel(String id,
			IModel<T> model, IPanelFactory fields);
}
