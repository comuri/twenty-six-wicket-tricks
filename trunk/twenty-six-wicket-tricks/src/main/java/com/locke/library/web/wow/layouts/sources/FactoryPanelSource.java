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
package com.locke.library.web.wow.layouts.sources;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;

import com.locke.library.web.wow.layouts.IPanelSource;
import com.locke.library.web.wow.layouts.IPanelIdentifierSource;
import com.locke.library.web.wow.panels.IPanelFactory;
import com.locke.library.web.wow.panels.IPanelFactorySource;

public class FactoryPanelSource implements IPanelSource {

	private final IPanelFactorySource factorySource;

	public FactoryPanelSource(IPanelFactorySource factorySource) {
		this.factorySource = factorySource;
	}

	public List<Panel> panels(IPanelIdentifierSource ids) {
		final List<Panel> panels = new ArrayList<Panel>();
		for (IPanelFactory<?> factory : factorySource.factories()) {
			panels.add(factory.newPanel(ids.nextId()));
		}
		return panels;
	}
}