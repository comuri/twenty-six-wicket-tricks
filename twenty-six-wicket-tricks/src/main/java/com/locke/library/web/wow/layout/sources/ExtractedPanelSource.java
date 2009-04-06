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
package com.locke.library.web.wow.layout.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;

import com.locke.library.web.wow.extraction.IPanelExtractor;
import com.locke.library.web.wow.layout.IPanelSource;

public class ExtractedPanelSource implements IPanelSource {

	private final Iterator<IPanelExtractor<?>> factories;

	public ExtractedPanelSource(final Iterator<IPanelExtractor<?>> factories) {
		this.factories = factories;
	}

	public List<Panel> panels(final Iterator<String> ids) {
		final List<Panel> panels = new ArrayList<Panel>();
		while (this.factories.hasNext()) {
			final IPanelExtractor<?> factory = this.factories.next();
			panels.add(factory.extract(ids.next()));
		}
		return panels;
	}
}
