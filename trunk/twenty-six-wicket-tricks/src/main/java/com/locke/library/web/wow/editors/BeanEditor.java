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

import com.locke.library.web.panels.IPanelFactory;
import com.locke.library.web.wow.component.extractors.bean.BeanExtractor;
import com.locke.library.web.wow.layouts.IComponentSource;
import com.locke.library.web.wow.layouts.sources.FactoryPanelSource;

public class BeanEditor<T> extends AbstractEditor<T> {

	private static final long serialVersionUID = 5714763871963883362L;

	private static final BeanExtractor.Settings defaultSettings = new BeanExtractor.Settings();

	public BeanEditor(final String id, IModel<T> model) {
		this(id, model, defaultSettings);
	}

	public BeanEditor(final String id, IModel<T> model,
			final BeanExtractor.Settings settings) {
		this(id, model, new FactoryPanelSource(new BeanExtractor(settings,
				model)));
	}

	public BeanEditor(final String id, IModel<T> model,
			IComponentSource<Panel> panels) {
		super(id, model, panels);
	}

	@Override
	protected AbstractFormPanel<T> newFormPanel(String id, IModel<T> model,
			IPanelFactory fields) {
		return new BeanFormPanel<T>(id, model, fields);
	}
}
