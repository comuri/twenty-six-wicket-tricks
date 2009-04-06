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
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;

import com.locke.library.web.panels.empty.EmptyPanel;
import com.locke.library.web.wow.layout.AbstractRepeaterLayout;
import com.locke.library.web.wow.layout.IPanelSource;

/**
 * An automatic layout that places panels in a grid that is columnCount cells
 * wide and any number of rows long.
 * 
 * @author Jonathan Locke
 */
public class ColumnLayout extends AbstractRepeaterLayout {

	private static final long serialVersionUID = 6099939141570065408L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            Component id
	 * @param panels
	 *            Source of panels
	 * @param columnCount
	 *            Number of columns in the grid
	 */
	public ColumnLayout(final String id, final IPanelSource panels,
			final int columnCount) {
		super(id, panels);

		if (columnCount < 1) {
			throw new IllegalArgumentException("Invalid column count: "
					+ columnCount);
		}

		final GridView<Panel> grid = new GridView<Panel>("rows", getProvider()) {

			private static final long serialVersionUID = -5420713157033336965L;

			@Override
			protected void populateEmptyItem(Item<Panel> item) {
				item.add(new EmptyPanel(CHILD_ID));
			}

			@Override
			protected void populateItem(Item<Panel> item) {
				item.add(item.getModelObject());
			}
		};
		grid.setRows(Integer.MAX_VALUE);
		grid.setColumns(columnCount);
		add(grid);
	}
}
