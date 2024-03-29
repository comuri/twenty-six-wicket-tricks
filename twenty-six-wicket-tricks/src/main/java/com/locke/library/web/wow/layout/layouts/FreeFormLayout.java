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
package com.locke.library.web.wow.layout.layouts;

import org.apache.wicket.markup.html.panel.Panel;

import com.locke.library.web.wow.extraction.IPanelExtractor;
import com.locke.library.web.wow.layout.AbstractLayout;
import com.locke.library.web.wow.layout.IPanelSource;

/**
 * A free-form layout allows a subclass to provide markup and define the precise
 * layout of the automatic components. Each component in the layout is simply
 * given the {@link IPanelExtractor#NAME} metadata value as its id.
 * 
 * @author Jonathan Locke
 */
public class FreeFormLayout extends AbstractLayout {

	private static final long serialVersionUID = 6099939141570065408L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            Component id
	 * @param source
	 *            Source of component factories
	 */
	public FreeFormLayout(final String id, final IPanelSource source) {
		super(id, source);
		for (Panel panel : getPanels()) {
			add(panel);
		}
	}
}
