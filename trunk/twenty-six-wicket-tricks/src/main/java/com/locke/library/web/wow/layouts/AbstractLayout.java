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
package com.locke.library.web.wow.layouts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AbstractLayout extends Panel {

	private static final long serialVersionUID = 1576711015939733296L;

	private final List<Panel> panels = new ArrayList<Panel>();

	static final String CHILD_ID = "panel";

	private final IDataProvider<Panel> provider = new IDataProvider<Panel>() {

		private static final long serialVersionUID = 3137979826799455116L;

		public void detach() {
		}

		public Iterator<Panel> iterator(int first, int count) {
			return getPanels().subList(first, first + count).iterator();
		}

		public IModel<Panel> model(Panel model) {
			return new Model<Panel>(model);
		}

		public int size() {
			return getPanels().size();
		}
	};

	public AbstractLayout(final String id, final IPanelSource source) {
		super(id);

		// Add components from the component source
		for (Panel panel : source.panels(new IPanelIdentifierSource() {
			public String nextId() {
				return CHILD_ID;
			}
		})) {
			getPanels().add(panel);
		}
	}

	protected List<Panel> getPanels() {
		return panels;
	}

	protected IDataProvider<Panel> getProvider() {
		return provider;
	}
}
